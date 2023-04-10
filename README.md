# WeatherProject
## Описание проекта
Проект WeatherProject представляет собой простое веб-приложение, которое позволяет пользователю получать информацию о погоде в разных городах мира. Пользователь может ввести название города в форму на главной странице, после чего приложение отправит запрос на сервер для получения информации о погоде в этом городе. Информация о погоде будет отображена на странице в удобном для пользователя виде.
## Описание кода
- Метод создания принимает аргументы из другой активности и хранит настройки в телефоне. В зависимости от того, есть ли аргументы, устанавливает город, проверяет наличие соединения с интернетом и получает данные с сервера или отображает начальный экран.
- Метод смены фрагмента создает новый фрагмент и передает ему bundle с настройками (в том числе JSON с кодом 404 или 200).
- Метод обработки нажатия кнопки поиска города вызывает новую активность с анимацией и завершает текущую активность.
- Метод проверки сети проверяет наличие соединения с интернетом. Для этого он использует системные параметры.
- Класс getData наследуется от AsyncTask и содержит методы preExecute, doInBackground, onPostExecute. В preExecute меняется текст на "ожидайте", а в doInBackground выполняется код для установки соединения по переданному URL, проверки длины полученной строки и возврата результата в виде строки или JSON с ошибкой.
## Функционал
### Приложение имеет следующие функции:
- Получение текущей погоды по названию города.
- Отображение текущей погоды, включая температуру, давление, влажность, скорость ветра и описание погодных условий.
- Отображение погоды на каждый из 5 следующих дней (в процессе разработки)
## Заключение
Проект WeatherProject является отличным примером простого веб-приложения, которое использует API для получения информации о погоде в реальном времени. Результаты запросов обрабатываются на сервере и возвращаются в формате JSON на клиентскую часть приложения, где они отображаются на странице. WeatherProject также демонстрирует простой, но эффективный дизайн, который использует изображения для создания привлекательного пользовательского интерфейса. В целом, проект WeatherProject является отличным примером простого, но полезного мобильного веб-приложения, которое может быть использовано для получения информации о погоде в любом городе мира.
