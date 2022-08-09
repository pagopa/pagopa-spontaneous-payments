// @ts-check
//  <ImportConfiguration>
const CosmosClient = require("@azure/cosmos").CosmosClient;
const config = require("./config");
const dbContext = require("./databaseContext");
//  </ImportConfiguration>

//  <DefineNewItem>
const newServiceItem = {
    "id": "service-1",
    "name": "Donation pagoPA service",
    "description": "Donation pagoPA service",
    "transferCategory": "tassonomia-1",
    "status": "ENABLED",
    "endpoint": "https://api.dev.platform.pagopa.it/gps/donation-service/v1",
    "basePath": "/donations/paymentoptions",
    "properties": [
        {
            "name": "amount",
            "type": "NUMBER",
            "required": true
        },
        {
            "name": "description",
            "type": "STRING"
        }
    ]
}
//  </DefineNewItem>

async function main() {

  // <CreateClientObjectDatabaseContainer>
  const { endpoint, key, databaseId, containerId } = config;

  const client = new CosmosClient({ endpoint, key });

  const database = client.database(databaseId);
  const container = database.container(containerId);

  // Make sure Tasks database is already setup. If not, create it.
  await dbContext.create(client, databaseId, containerId);
  // </CreateClientObjectDatabaseContainer>

  try {

    // <CreateItem>
    /** Create new item
     * newItem is defined at the top of this file
     */
    const { resource: createdItem } = await container.items.create(newServiceItem);
    console.log(`\r\nCreated new item: ${createdItem.id} - ${createdItem.description}\r\n`);
    // </CreateItem>

  } catch (err) {
    console.log(err.message);
  }
}

main();