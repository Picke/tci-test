Project Name

TCI Test Project

Installation

1. Go src/main/resources/config/app.properties and change "resource.dir" property to "resources" folder using absolute path.
2. To run collector execute command "mvn tomcat7:run -Dservice.name=collector" from project root directory
3. To run repositoryA execute command "mvn tomcat7:run -Dservice.name=repository -Drepository.name=A -Dmaven.tomcat.port=8083" from project root directory
4. To run repositoryB execute command "mvn tomcat7:run -Dservice.name=repository -Drepository.name=B -Dmaven.tomcat.port=8084" from project root directory
5. To run repositoryC execute command "mvn tomcat7:run -Dservice.name=repository -Drepository.name=C -Dmaven.tomcat.port=8085" from project root directory
(Use another ports if some is already in use)

Usage

- GET
	- GET http://localhost:8080/documents HTTP/1.1 
		/*will return all documents from all repositories*/
		
	- GET http://localhost:8080/documents/c0ae21f2-b70a-11e6-80f5-76304dec7eb7 HTTP/1.1 
		/*will return document with documentId: c0ae21f2-b70a-11e6-80f5-76304dec7eb7*/
	  

- POST 
	- POST http://localhost:8080/documents HTTP/1.1
	{
    "documentName": "Document 6",
    "documentTitle": "Title 6",
    "indexes": {
      "first": "1-100",
      "second": "101-200",
      "third": "201-300"
    },
    "documentContent": "Content 6",
    "comments": [
      {
        "userId": "3",
        "commentContent": "Comment content 9"
      },
      {
        "userId": "4",
        "commentContent": "Comment content 10"
      }
    ]
    }
		/*will generate documentId and commentIds, and save document by default to repositoryA*/
		
	- POST http://localhost:8080/documents?repositoryName=C HTTP/1.1
	{
    "documentName": "Document 6",
    "documentTitle": "Title 6",
    "indexes": {
      "first": "1-100",
      "second": "101-200",
      "third": "201-300"
    },
    "documentContent": "Content 6",
    "comments": [
      {
        "userId": "3",
        "commentContent": "Comment content 9"
      },
      {
        "userId": "4",
        "commentContent": "Comment content 10"
      }
    ]
    }
		/*will generate documentId and commentIds, and save document to repositoryC	*/
		
- PUT
	- PUT http://localhost:8080/documents/c0ae21f2-b70a-11e6-80f5-76304dec7eb7 HTTP/1.1
	{
    "documentName": "Document 16",
    "documentTitle": "Title 16",
    "indexes": {
      "first": "1-100"
    },
    "documentContent": "Content 16",
    "comments": [
      {
        "commentId": "bbcc5f8d-9c5e-45be-862b-d5b5484e6366",
        "userId": "3",
        "commentContent": "Comment content 19"
      }
    ]
  }
		/*will update document with documentId: c0ae21f2-b70a-11e6-80f5-76304dec7eb7 if exists*/
	
-DELETE 
	- DELETE http://localhost:8080/documents/c0ae21f2-b70a-11e6-80f5-76304dec7eb7 HTTP/1.1
		/*will delete document with documentId: c0ae21f2-b70a-11e6-80f5-76304dec7eb7 if exists*/
