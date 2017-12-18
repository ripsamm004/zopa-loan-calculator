# ZOPA LOAN CALCULATOR


DEFAULT COMPOUND INTEREST FORMULA:   A = P(1+r/n)^nt
ZOPA COMPOUND INTEREST FORMULA   :   A = P(0.9970221+r/n)^nt

Where
P = loan amount, r = rateInDecimal, t = number of year,
n = number of compound per unit 12 month and A = Total to pay by borrower



## Requirements

Java 7 or Above


## Command Line Arguments
* market.csv        path to of the csv file
* 1500              amount of loan


## Example
```
java -jar loan-calculator-1.0.0-jar-with-dependencies.jar market.csv 1000
```
