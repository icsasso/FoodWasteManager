from fastapi import FastAPI
from pymongo import MongoClient
from pymongo.errors import PyMongoError
from dotenv import load_dotenv
from pydantic import BaseModel
import os

# Load environment variables from .env
load_dotenv()

# FastAPI app
app = FastAPI()

# Function to connect to MongoDB
def get_connection():
    mongo_url = os.getenv("MONGO_URL")
    try:
        client = MongoClient(mongo_url, serverSelectionTimeoutMS=5000)
        # Ping the server to check connection
        client.admin.command("ping")
        print("Pinged your deployment. You successfully connected to MongoDB!")
        return client
    except PyMongoError as e:
        print("Error connecting to MongoDB:", e)
        return None

# Function to get items from the DB
from bson import ObjectId

def get_items_in_db():
    client = get_connection()
    if not client:
        return []

    db = client["ingredients"]
    collection = db["items"]

    try:
        documents = list(collection.find())
        items = []
        for doc in documents:
            # Convert ObjectId to string
            doc["_id"] = str(doc["_id"])
            items.append(doc)
            print(doc)
        return items
    except PyMongoError as e:
        print("Error fetching documents:", e)
        return []
    finally:
        client.close()

# FastAPI route
@app.get("/items")
def read_items():
    return get_items_in_db()

food_list = []
class FoodItem(BaseModel):
    name:str
    exipration_date: str

@app.post("/insert")
def insert_food(item: FoodItem):
    food_list.append(item.dict())
    return {"message": "Food item inserted successfully", "item": item}

@app.delete("/delete/{item_name}")
def delete_food(item_name: str):
    global food_list
    for item in food_list:
        if item["name"] == item_name:
            food_list.remove(item)
            return {"message": f"{item_name} deleted successfully"}
    return {"error": f"{item_name} not found"}

# Simple "Hello World" route
@app.get("/")
def hello():
    return {"message": "Hello World"}
