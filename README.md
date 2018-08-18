Кэш в разделяемой памяти
====================

[Оригинальный проект Андрея Паньгина](https://github.com/odnoklassniki/shared-memory-cache) тестировался 
на JDK 7, а также сравнивался с ранними версиями библиотек Ehcache и JCS (актуальными на 2012 год). Конкретные
версии библиотек не указаны, отсутствует файл конфигурации JCS.

Текущий проект запускался на OpenJDK 11, использовались последние версии библиотек Ehcache и JCS
(3.5.2 и 2.2)

Для компилирования и запуска теста на jdk11 необходимо в командную строку
добавлять следующие параметры:<br/>
--add-exports java.base/sun.nio.ch=ALL-UNNAMED --add-exports jdk.unsupported/sun.misc=ALL-UNNAMED --add-exports java.base/jdk.internal.ref=ALL-UNNAMED --add-modules=jdk.unsupported
<br/>(они уже прописаны в gradle- и maven-конфигурации).

Для открытия проекта в Idea следует импортировать из pom.xml, потому что из gradle-конфигурации Idea не подтягивает
необходимые аргументы компилирования.

Результаты тестирования:
  

JCSCache write: 10564<br/>
JCSCache read: 1293<br/>
JCSCache read-write: 6746<br/>

Ehcache write: 12364 <br/>
Ehcache read: 2052 <br/>
Ehcache read-write: 3210 <br/>

ConcurrentHashMapCache write: 43827<br/>
ConcurrentHashMapCache read: 268<br/>
ConcurrentHashMapCache read-write: 2239<br/>

UnsafeMemoryCache write: 4673<br/>
UnsafeMemoryCache read: 1194<br/>
UnsafeMemoryCache read-write: 1446<br/>


