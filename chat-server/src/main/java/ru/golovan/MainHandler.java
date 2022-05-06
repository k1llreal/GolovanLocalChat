package ru.golovan;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;


//обработчик
public class MainHandler extends SimpleChannelInboundHandler<String> {

    //список всех клиентов которые к нам подключились
    //чтобы рассылать сообщения всем клиентам
    private static final List<Channel> channels = new ArrayList<>();
    private static int newClientIndex = 1;
    private String clientName;

    //срабатывает при подключении клиента
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //при подключении клиента добавляем канал в список
        channels.add(ctx.channel());

        //нумеруем всех подключающихся клиентов
        clientName = "Клиент #" + newClientIndex;
        newClientIndex++;

        //сообщаем всем о подключении клиента на сервере
        System.out.println(clientName + " подключился");

        //при подключении нового клиента сервер уведомляет всех в чате об этом
        broadcastMessage("SERVER", "Подключился новый клиент: " + clientName);
    }

    //срабатывает когда клиент присылает какое-либо сообщение
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {

        //если сообщение начинается со / то оно командное
        if (s.startsWith("/")) {
            //смена имени пользователя
            //при вводе /renameTo kirill имя клиента меняется на kirill
            if (s.startsWith("/renameTo")) {
                String newClientName = s.split("\\s", 2)[1];
                //сообщаем всем о смене имени клиента
                broadcastMessage("SERVER", clientName + " сменил имя на " + newClientName);
                clientName = newClientName;

            }
            return;
        }

        //рассылаем сообщение всем клиентам
        broadcastMessage(clientName, s);
    }

    //метод который позволяет от имени clientName разослать сообщения message
    public void broadcastMessage(String clientName, String message) {
        String out = String.format("[%s]: %s\n", clientName, message);

        //проходимся по всем каналам и в каждый канал отправляем сообщение
        for (Channel c : channels) {
            c.writeAndFlush(out);
        }
    }

    //при закрытии канала, т.е. выхода клиента из сети, мы уведомляем всех об этом
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //сообщаем всем о выходе клиента на сервере
        System.out.println("Клиент " + clientName + " вышел из сети");

        //удаляем клиента из списка
        channels.remove(ctx.channel());

        //сообщаем всем о выходе клиента в чат
        broadcastMessage("SERVER", "Клиент: " + clientName + " вышел из сети");

        //прекращаем работу с клиентом
        ctx.close();
    }

    //в этом методе мы узнаем о возникновении ошибки и выходе клиента
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("Клиент " + clientName + " отвалился");

        //удаляем клиента из списка
        channels.remove(ctx.channel());

        //сообщаем всем о выходе клиента
        broadcastMessage("SERVER", "Клиент: " + clientName + " вышел из сети");

        //прекращаем работу с клиентом
        ctx.close();

    }
}
