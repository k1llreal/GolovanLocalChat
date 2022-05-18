# GolovanLocalChat
Клиент-серверный чат для локальной сети на Java с использованием JavaFX(графический интерфейс) и Netty(фреймворк для  работы с сетью).  <br>
Структура имеет два раздела: сервер и сам клиент. <br>
Сначала нужно запустить сервер и после этого уже запускать клиенты. <br>
Хост и порт указываются хардкодом константой на сервере в классе ServerApp, для клиента в классе Network. <br>
Из доп. функций в чате клиент может менять имя пользователя посредством команды /renameTo ИМЯ <br>
Также при входе и выходе клиента сервер уведомляет об этом всех, кто присутствует в чате. <br>
Программа написана в качестве лабораторной работы по предмету архитектура информационных систем. <br>

Визуальная часть--> 
(в примере запущен сервер и 3 клиента, в первом клиенте было изменено имя пользователя на Kirill,
на третьем клиенте был осуществлён выход, для демонстрации полной работы сервера.)
![localchat](https://user-images.githubusercontent.com/90712664/167130363-2157f9e0-8720-46d2-8c51-822ac90f2768.png)
