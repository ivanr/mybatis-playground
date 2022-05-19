# MyBatis Notes

MyBatis is a library for OR mapping that tries to do as little as possible. It largely focuses on providing helper functionality to map query results to Java objects. How it works:

* MyBatis has a concept of mappers, which are Java interfaces that define what named queries exist. MyBatis generates the mapping code on the fly.

   * Mappers should be kept small, grouping related queries. Caching—if using—is configured 
   on a per-mapper basis.

* Named queries are written in SQL. There are two configuration formats, one that uses XML and another based on Java annotations. While the XML format is more powerful, the annotations are easier to use because the SQL statements are in the same file as the Java code (interfaces).

* When a query runs, MyBatis transforms the results into Java object. There are built-in mappers for the common types: http://www.mybatis.org/mybatis-3/configuration.html#typeHandlers 

* Type handlers can be written to handle custom mapping. They are triggered based on the Java type (on serializating to SQL) or JDBC type (on deserialization into objects), or can be explicitly requested.

   * **WARNING** By default, MyBatis uses introspection to determine which handler to use. Because of type erasure, type handlers that deal with collections are potentially dangerous, as MyBatis may pick the wrong handler. For best results, always use explicit mapping, either via the @MapTypes annotation, or in the SQL. Disable implicit mapping in all other cases.

## Object Validation After Deserialization

**TODO** It would be nice to be able to validate objects after they are created by MyBatis, maybe even before they're sent to the database. One way to do this would be to use a proxy around the generated mapper, which could check if the manipulated objects support validation, and invoke it.

## Method Name Convention

Use the following name conventions for the mapper methods; note that MyBatis doesn't currently support method overloading, but that shouldn't be a big problem if the mappers are kept small (e.g., one mapper per object type).

* selectById
* selectAll
* insert
* insertAll(Iterable<T>)
* update(T)
* delete(T)
* deleteAll()
* deleteAll(Iterable<T>)
* deleteById
* existsById

## Generated Keys

Primary keys generated by the database can be obtained after insert with the following annotation option:

```
@Options(useGeneratedKeys = true,
        keyProperty = "reviewId",
        keyColumn = "review_id")
```        

The above requires support from the database driver, and Postgres does. With some other database, it may be possible to use the @SelectKey annotation.
 
## Parameter Names

Java by default doesn't include method parameter names in the bytecode. Compile with the "-parameters" option so that MyBatis can retrieve parameter names to use in SQL.

## Gson Integration

Gson can be integrated via custom type handlers. See the AbstractJsonTypeHandler, AuthorAsJson, and StringListAsJsonArray classes for an example.

## Dynamic SQL Generation

MyBatis mappers support dynamic SQL generation via the @InsertProvider, @UpdateProvider, @DeleteProvider, and @SelectProvider annotations. For example:

```
@SelectProvider(type=DynamicProvider.class, method="methodToInvoke")
Review selectByIdAndAnother(int reviewId, String condition);
```

## Avoiding Result Set Buffering

By default, both the database driver and MyBatis may buffer the result set. Use the following approach to control the buffering:

```
@Select("SELECT * FROM large_table")
@ResultType(ResultType.class)
@Options(fetchSize = 1000)
void selectAll(ResultHandler handler);
```

## Reloading Mappers in Development

Generated mappers are cached on a per-connection basis. In development mode, if the code is updated, a new connection must be created for the new code to be activated.

## Immutable Objects

MyBatis can create immutable objects provided it can find a constructor it can use. One option is to provide an all-args constructor that sets all the fields. A slightly weaker approach would be to use a no-args constructor, after which the fields can be set directly (via reflection, even if they're private).

Another option is to explicitly configure which constructor should be used, for example:

```
@ConstructorArgs({
   @Arg(column = "col2", name = "second", javaType = String.class),
   @Arg(column = "col1", name = "first", javaType = Integer.class)
})
```

## SQL Editing in IDEA

IDEA has support for SQL editing, which will work with SQL in standalone files, as well as when SQL is embedded in the code. The latter works after IDEA is made aware of the embedded SQL. For more information, see here:

* https://www.jetbrains.com/help/idea/using-language-injections.html 

IDEA doesn't support MyBatis by default, so it needs to be taught to recognise the #{...} and ${...} patterns for statement parameters. In Preferences > Tools > Database > User Parameters, add the following two patterns:

```
    #\{([^\{\}]+)\}
    \$\{([^\{\}]+)\}
```
    
Check both checkboxes.

Formatting:
* Leave simple queries as a single line
* Use IDEA's option for SQL formatting on everything else

## Returning

MyBatis doesn't support RETURNING directly, but there is a [workaround](https://github.com/mybatis/mybatis-3/issues/1293). The developers didn't explain why the flush is necessary. In my tests, works as intended without the flush and with caching disabled. There is also [this bug](https://github.com/mybatis/mybatis-3/issues/2156) report that says that MyBatis will not see anything in @Select as an update, meaning it won't rollback everything in certain situations.

```
// This query uses RETURNING. For MyBatis to handle, we must use @Select + flush cache.
// See here for more information: https://github.com/mybatis/mybatis-3/issues/1293
@Select("INSERT INTO ... RETURNING ...")
@Options(flushCache = Options.FlushCachePolicy.TRUE)
boolean insertWithReturning(XYZ xyz);
```

## Mapper Code Generation

**TODO** Writing mappers by hand is boring; perhaps we can generate them automatically? Something along these lines:

1. Input:
   1. Source package + list of class names
   2. Destination package
   3. Options
2. Generate one lower-level (internal) interface for CRUD operations; one per class.
3. Generate an outer layer-interface to be referenced directly, but only if it doesn't exist already. Additional custom code can be added here. This interface inherits from the base one.
4. Will need to be able to determine PK and tenant ID, probably via annotations.
5. Option to transform field names; transient fields excluded.
6. Tenant ID presence detected automatically.
7. Mapping:
   1. Standard classes directly
   2. Lists via SQL arrays or JSON arrays (depending on the JDBC type)
   3. Complex objects via JSON
   4. In generated SQL, cast to the correct enum
8. It would be best to also connect to the database to examine the JDBC types.
9. Gradle and IDEA annotation processors are a possibility.

We could probably do all of this without access to the database with some additional annotations.