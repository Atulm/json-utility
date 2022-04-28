# json-utility
JSON utility to reduce input JSON based on some attributes

Prerequiste
- Intellij/NetBeans/Eclipse any tool
- Java 11 (Using Intellij 2019 and its giving issues with Java 17,Due to Current project dependencies couldn't install the updated one by removing the current version)
- Maven

To filter out the JSON, we are using JSON Path to extract the attributes and build a output JSON.

------------------------------------------------------------------------------------------------
filters/JSON path can be given in the request body seperated by comma

Input Parameters:  
**Endpoint:** http://localhost:9192//transform
**Request Body:** 
{  "filters":"$.catalogVersion,$.productContentQuality,$.lastEditedTime,$.isSupplierImagesAvailable,$.lastEditedUser,$.dueDateRequiresEval,$.isSupplierContentAvailable,$.hasImportErrors,$.shortCode,$.images[0].clippingPath,$.images[2].modifiedTime,$.images[*].index,$.images[*].url,$.offlineDate,$.classificationAttributeList[0].fullQualifier"
}
------------------------------------------------------------------------------------------------

For reference, I have also added a Postman collection which can be directly imported and try to see the working once the application is up.
**JSON Transformation.postman_collection.json**
_
** In case you want to run your application on different port, this can be changed by updating "server.port=9192" property kept inside /resource/application.properties_


