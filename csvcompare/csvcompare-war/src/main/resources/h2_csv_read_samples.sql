/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Author:  berni3
 * Created: Jun 20, 2019
 */

/*
CSV (Comma Separated Values) Support

The CSV file support can be used inside the database using the functions 
CSVREAD and CSVWRITE, or it can be used outside the database as a standalone tool.
Reading a CSV File from Within a Database

A CSV file can be read using the function CSVREAD. Example:

SELECT * FROM CSVREAD('test.csv');

Please note for performance reason, CSVREAD should not be used inside a join. 
Instead, import the data first (possibly into a temporary table), 
create the required indexes if necessary, and then query this table.

Importing Data from a CSV File

A fast way to load or import data (sometimes called 'bulk load') from 
a CSV file is to combine table creation with import. 
Optionally, the column names and data types can be set when creating the table. 
Another option is to use INSERT INTO ... SELECT.

CREATE TABLE TEST AS SELECT * FROM CSVREAD('test.csv');
CREATE TABLE TEST(ID INT PRIMARY KEY, NAME VARCHAR(255))
    AS SELECT * FROM CSVREAD('test.csv');

Writing a CSV File from Within a Database

The built-in function CSVWRITE can be used to create a CSV file from a query. Example:

CREATE TABLE TEST(ID INT, NAME VARCHAR);
INSERT INTO TEST VALUES(1, 'Hello'), (2, 'World');
CALL CSVWRITE('test.csv', 'SELECT * FROM TEST');
*/
/*--- ---*/
DROP TABLE IF EXISTS CSV_READ_1;

CREATE TABLE CSVREAD_1 AS
SELECT * FROM CSVREAD('C:/Users/berni3/Documents/GitHub/CsvCompare/csvcompare/csvcompare-war/src/main/resources/csv-sample1.csv',
null,
'charset=UTF-8 fieldSeparator=|');

SELECT COUNT(*) AS COUNT, VON FROM CSVREAD_1 
GROUP BY VON
ORDER BY 1 DESC;

/*--- ---*/
DROP TABLE IF EXISTS CSV_READ_2;

CREATE TABLE CSVREAD_2 AS
SELECT * FROM CSVREAD('C:/Users/berni3/Documents/GitHub/CsvCompare/csvcompare/csvcompare-war/src/main/resources/csv-sample2.csv',
null,
'charset=UTF-8 fieldSeparator=|');


/*---
Joins a table. The join expression is not supported 
for cross and natural joins. 
A natural join is an inner join, where the condition is automatically on the columns with the same name.

Example:

TEST AS T LEFT JOIN TEST AS T1 ON T.ID = T1.ID

LEFT	
RIGHT	
	OUTER	
	
	INNER	
	
CROSS	
NATURAL
 ---*/

/*--- */
SELECT 'I' AS JOINMODE, CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
INNER JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF
UNION
SELECT 'L', CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
LEFT JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF
UNION
SELECT 'R', CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
RIGHT JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF 
ORDER BY 1;

/*--- */
DROP TABLE IF EXISTS CSV_1_2_RESULT1 ;

CREATE TABLE CSV_1_2_RESULT1 (
JOINMODE VARCHAR2(1),
CSV1_ZEIT VARCHAR2(100),
CSV1_VON VARCHAR2(100),
CSV1_BETREFF VARCHAR2(100),
CSV2_ZEIT VARCHAR2(100),
CSV2_VON VARCHAR2(100),
CSV2_BETREFF VARCHAR2(100)
);

INSERT INTO CSV_1_2_RESULT1
SELECT 'I' AS JOINMODE, CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
INNER JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF
UNION
SELECT 'L', CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
LEFT JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF
UNION
SELECT 'R', CSV1.*, CSV2.*
FROM CSVREAD_1 CSV1
RIGHT JOIN CSVREAD_2 CSV2 
ON CSV1.VON = CSV2.VON AND CSV1.BETREFF = CSV2.BETREFF 
ORDER BY 1;

CALL CSVWRITE('result.csv', 'SELECT * FROM CSV_1_2_RESULT1');