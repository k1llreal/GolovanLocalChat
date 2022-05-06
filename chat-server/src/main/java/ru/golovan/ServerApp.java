package ru.golovan;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ServerApp {
    private static final int PORT = 8189;

    public static void main(String[] args) {
        //создаем два менеджера потоков для обработки задач в параллельных потоках

        //отвечает за подключающихся клиентов, поэтому достаточно 1 потока
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);

        //отвечает за обработку данных (по умолчанию создаст около 20-30 потоков)
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //создаем ServerBootstrap для преднастройки сервера (например для указания порта, для каналов и опций)
            ServerBootstrap b = new ServerBootstrap();
            System.out.println("Сервер запущен");
            b.group(bossGroup, workerGroup) //указываем серверу чтобы он использовал два менеджера потоков
                    .channel(NioServerSocketChannel.class) //используем канал для подключения клиентов
                    .childHandler(new ChannelInitializer<SocketChannel>() { //в SocketChannel лежит информация о соединении клиента
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new StringDecoder(), new StringEncoder(), new MainHandler()); //для каждого клиента свой конвеер
                        }
                    }); //при подлючении клиента настраиваем процесс общения

            ChannelFuture future = b.bind(PORT).sync(); //запуск сервера на порту 8189
            future.channel().closeFuture().sync(); //ожидание закрытия, чтобы сервер не закрывался сразу
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //закрываем пулы потоков при остановке сервера
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
