# Gitlet Design Document

**Name**:

## Classes and Data Structures

### Commit

#### Instance Variables

1. String message
2. Date timestamp
3. Reference to parent
4. References to blobs

#### Methods

1. Default Constructor: for initial Commit
2. Constructor for general Commit
3. 

### Repository

#### Instance Variables

1. 

#### Methods

1. void init()
2. 

### Blob

#### Instance Variables

1. String message

#### Methods

1.


## Algorithms

## Persistence
* .gitlet directory
* commits directory
  * files named by hash
  * serialized commit in each file
* staging directory
  * save file directly (with original naming) in staging
  * when file with same name is being added, compare hash
* blobs directory
  * directries named by hash for each file
  * single file in each directory

