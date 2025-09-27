from fastapi import FastAPI
from pymongo import MongoClient
from pymongo.errors import PyMongoError
from dotenv import load_dotenv
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

# Simple "Hello World" route
@app.get("/")
def hello():
    return {"message": "Hello World"}
