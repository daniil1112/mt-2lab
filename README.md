# Лабораторная работа №2. Ручное построение нисходящих синтаксических анализаторов

## Вариант 2. Регулярные выражения

Регулярные выражения с операциями конкатенации (простая последовательная запись строк), выбора (вертикальная черта),
замыкания
Клини. Приоритет операций стандартный. Скобки могут использоваться
для изменения приоритета.
Для обозначения базовых языков используются маленькие буквы латинского алфавита. Используйте один терминал для всех
символов.
Пример: `((abc*b|a)*ab(aa|b*)b)*`

## Программа и запуск:

- [Main class](src/main/kotlin/Main.kt)
- [LexicalAnalyzer](src/main/kotlin/parser/LexicalAnalyzer.kt)
- [Parser](src/main/kotlin/parser/Parser.kt)
- Запуск gradle\после
  компиляции `java --jar program.jar <expression> <filename> [--withEmptyTerminals] [--withEmptyNodes]`
- `--withEmptyTerminals` - вывести в результат EMPTY terminal (`ε`)
- `--withEmptyNodes` - вывести в результат ноды, у которых дети EMPTY terminal или нет детей

## Тесты:

- [LexicalAnalyzerTest](src/test/kotlin/LexicalAnalyzerTest.kt)
- [ParserTest](src/test/kotlin/ParserTest.kt)

## Грамматика

Рассчитывал, что мы смотрим на правила регулярок в питоне, там запрещено **, ||, но это не сложно добавить
(1 доп нетерминал)

| Нетерминал                                      | Описание нетерминала                                          |
|-------------------------------------------------|---------------------------------------------------------------|
| `Or -> Concat Or'`                              | Задает первую половину or                                     |
| `Or' -> '\|' Concat Or'`<br/> `Or' -> ε`        | Задает продолжение or или останавливает парсинг or            |
| `Concat -> Clini Concat'`                       | Задает начало конкатенации                                    |
| `Concat' -> Clini Concat'`<br/> `Concat -> ε`   | Задает продолжение concat или останавливает парсинг concat    |
| `Clini -> Base Clini'`                          | Задает начало замыкания клини                                 |
| `Clini' -> '*'`<br/> `Clini' -> ε`              | Задает продолжение замыкания клини, или останавливает парсинг |
| `Base -> '(' Or ')'`<br/> `Base -> var literal` | Задает переменную или новое выражение в ()                    |

## FIRST и FOLLOW

| Нетерминал | FIRST               | FOLLOW                              |
|------------|---------------------|-------------------------------------|
| `Or`       | `(`, `literal`      | `$`, `)`                            |
| `Or'`      | `\|`, `ε`           | `$`, `)`                            |
| `Concat`   | `(`, `literal`      | `$`,`\|`, `)`                       |
| `Concat'`  | `(`, `literal`, `ε` | `$`,`\|`, `)`                       |
| `Clini`    | `(`, `literal`      | `$`, `(`, `\|`, `)`, `literal`      |
| `Clini'`   | `*`, `ε`            | `$`, `(`, `\|`, `)`, `literal`      |
| `Base`     | `(`, `literal`      | `$`, `(`, `\|`, `)`, `literal`, `*` |


LL(1)-грамматика:
1. A→a, A→β,A∈N ⇒ FIRST(α)∩FIRST(β)=∅
2. A→α, A→β,A∈N, ε∈FIRST(α) ⇒ FOLLOW(A)∩FIRST(β)=∅
=><br/> Это LL(1) грамматика

## Дерево разбора примера
- [Полное](exampleFULL.png) без сокращений 
- [Частичное](withoutEmptyTerminals.png) убраны ε ноды
- [Частичное](short.png) убраны ε ноды + пустые нетерминалы