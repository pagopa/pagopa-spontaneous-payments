from azure.cosmos import exceptions, CosmosClient, PartitionKey
import os
from dotenv import load_dotenv

load_dotenv()

URI = os.getenv('URI')
PRIMARY_KEY = os.getenv('PRIMARY_KEY')
DB_NAME = os.getenv('DB_NAME')
CONTAINER_NAMES = os.getenv('CONTAINER_NAMES')
PARTITION_KEYS = os.getenv('PARTITION_KEYS')

# Initialize the Cosmos client
endpoint = URI
key = PRIMARY_KEY

# <create_cosmos_client>
client = CosmosClient(endpoint, key)
# </create_cosmos_client>

# Create a database
# <create_database_if_not_exists>
database_name = DB_NAME
database = client.create_database_if_not_exists(id=database_name)
# </create_database_if_not_exists>

# Create a container
# Using a good partition key improves the performance of database operations.
# <create_container_if_not_exists>
container_names = CONTAINER_NAMES.split(',')
partition_keys = PARTITION_KEYS.split(',')

containers_infra = zip(container_names, partition_keys)

for container in containers_infra:
    container = database.create_container_if_not_exists(
        id=container[0], # container name
        partition_key=PartitionKey(path=f"/{container[1]}"),
        offer_throughput=400
    )
# </create_container_if_not_exists>
