docker compose up -d

# await 15s
echo "Waiting for 15 seconds..."
sleep 15
echo "Done waiting."

# Create Table Users
aws --endpoint-url="http://localhost:4566" dynamodb create-table \
  --region "sa-east-1" \
  --table-name "tb_users" \
  --attribute-definitions \
    AttributeName=user_id,AttributeType=S \
    AttributeName=email,AttributeType=S \
  --key-schema \
    AttributeName=user_id,KeyType=HASH \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --global-secondary-indexes \
    "[
      {
        \"IndexName\": \"email-index\",
        \"KeySchema\": [
          {\"AttributeName\": \"email\", \"KeyType\": \"HASH\"}
        ],
        \"Projection\": {
          \"ProjectionType\": \"INCLUDE\",
          \"NonKeyAttributes\": [\"user_id\",\"password\"]
        },
        \"ProvisionedThroughput\": {
          \"ReadCapacityUnits\": 5,
          \"WriteCapacityUnits\": 5
        }
      }
    ]" \
  --query 'TableDescription.TableName' \
  --output text

#Create table Tb Users Links

aws --endpoint-url="http://localhost:4566" dynamodb create-table \
    --region "sa-east-1" \
    --table-name tb_user_links \
    --attribute-definitions \
        AttributeName=link_id,AttributeType=S \
        AttributeName=user_id,AttributeType=S \
    --key-schema \
        AttributeName=link_id,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --global-secondary-indexes \
        '[
            {
                "IndexName": "fk-user-index",
                "KeySchema": [
                    {
                        "AttributeName": "user_id",
                        "KeyType": "HASH"
                    },
                    {
                       "AttributeName": "link_id",
                       "KeyType": "RANGE"
                    }
                ],
                "Projection": {
                    "ProjectionType": "ALL"
                },
                "ProvisionedThroughput": {
                    "ReadCapacityUnits": 5,
                    "WriteCapacityUnits": 5
                }
            }
        ]' \
    --query 'TableDescription.TableName' \
    --output text

# Create Link Analytics Table
aws --endpoint-url="http://localhost:4566" dynamodb create-table \
  --region "sa-east-1" \
  --table-name tb_links_analytics \
  --attribute-definitions \
    AttributeName=link_id,AttributeType=S \
    AttributeName=date,AttributeType=S \
  --key-schema \
    AttributeName=link_id,KeyType=HASH \
    AttributeName=date,KeyType=RANGE \
  --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --query 'TableDescription.TableName' \
  --output text