
# Проект Pet Crypto Viewer 

Проект является REST API сервисом для получения стоимости желаемой
криптовалюты на данный момент(162 валютные пары). Под капотом он взаимодействует с API 
blockchain.com, откуда берет значения, сохраняет в БД и затем выдает
пользователю. Для хранения данных используются две таблицы, одна - где перечислены все валюты, другая - где сохраняются значения всех валютных пар во времени, они автоматически заполняются и пополняются.

## Функциональность:

-Выдача последнего во времени значения валютной пары из базы данных пользователю. Если последнее значение старше 30 минут, то от внешнего API запрашивается более свежее значение.
-Вычисление скользящей средней по конкретной валюте к доллару и с временным интервалом в 10,30,60,720,1440,10080 минут. То есть берем из БД все значения валюты с заданным временным интервалом на выбранном временном диапазоне и получаем среднее арифметическое. 

## Стек:

-Java 17
-Spring Boot
-Spring MVC
-Hibernate
-MySQL
-Docker
-Gradle
-Git
-Thymeleaf

## Как сделать быстрый деплой на свою машину:

1) Перейти в папку forDeploying
2) Скопировать файлы docker-compose.yml и .env в новую директорию
3) Выполнить docker-compose up находясь в этой директории
p.s Приложение будет настроено на порт 8080, БД на 3306
