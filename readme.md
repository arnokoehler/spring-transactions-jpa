
# So there are 3 types of issues with transactions:


## Dirty Reads

|Transaction 1   |  Transaction 2 |
|---|---|
| Select * From DATA where (gives 20) |   |
|  | UPDATE DATA SET x + 1 WHERE z  |
| Select * From DATA where (gives 21) |   |

Now if Transaction 2 roles back Transaction 1 contains a dirty read

----

## Non Repeatable Reads

|Transaction 1   |  Transaction 2 |
|---|---|
| Select * From DATA where (gives 20) |   |
|  | UPDATE DATA SET x + 1 WHERE z |
|  | COMMIT; |
| Select * From DATA where (gives 21) |   |

Now Transaction 1 contains a non repeatable read

----

## Phantom Reads


|Transaction 1   |  Transaction 2 |
|---|---|
| Select * From DATA where (gives 200 rows) Limit 10 |   |
|  | INSERT 20 ROWS |
|  | COMMIT; |
| Select * From DATA where (gives 210 rows) Limit 20 |   |
| result set contains overlap and not all records |   |

----

### Table locking techniques to prevent this

Databases can use table or row locks to prevent these kind of issues, 
as you can understand the higher the locking level - the slower the performance

* Read uncommitted 
    * permits dirty reads, non repeatable reads and phantom reads.
* Read committed 
    * permits non repeatable reads and phantom reads.
* Repeatable read 
    * permits only phantom reads.
* Serializable 
    * does not permit any read errors.
    
---

Well let's see what would suits our use-case:

* POSTGRES default = Read committed
* ORACLE Read commited OR serializble (NO OTHER SUPPORT)

So it doesn't feel like we should really deep dive into the others besides 
READ COMMITTED

(change my mind ...)
