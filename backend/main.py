from fastapi import FastAPI, HTTPException
from pymongo import MongoClient
from pymongo.errors import PyMongoError
from dotenv import load_dotenv
from pydantic import BaseModel
import os
from datetime import datetime

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

# Function to insert item into MongoDB
def insert_item_to_db(food_item: dict):
    client = get_connection()
    if not client:
        raise HTTPException(status_code=500, detail="Database connection failed")
    
    db = client["ingredients"]
    collection = db["items"]
    
    try:
        # Add timestamp for when item was added
        food_item["created_at"] = datetime.now().isoformat()
        result = collection.insert_one(food_item)
        return str(result.inserted_id)
    except PyMongoError as e:
        print("Error inserting document:", e)
        raise HTTPException(status_code=500, detail="Failed to insert item")
    finally:
        client.close()

# Function to delete item from MongoDB
def delete_item_from_db(item_name: str):
    client = get_connection()
    if not client:
        raise HTTPException(status_code=500, detail="Database connection failed")
    
    db = client["ingredients"]
    collection = db["items"]
    
    try:
        result = collection.delete_one({"name": item_name})
        return result.deleted_count > 0
    except PyMongoError as e:
        print("Error deleting document:", e)
        raise HTTPException(status_code=500, detail="Failed to delete item")
    finally:
        client.close()

# Pydantic models
class FoodItem(BaseModel):
    name: str
    expiration_date: str  # Fixed typo

class FoodItemResponse(BaseModel):
    id: str
    name: str
    expiration_date: str
    created_at: str

# FastAPI routes
@app.get("/")
def hello():
    return {"message": "Hello World"}

@app.get("/items")
def read_items():
    return get_items_in_db()

@app.post("/insert")
def insert_food(item: FoodItem):
    try:
        print(f"Received item: {item}")  # Debug log
        print(f"Item dict: {item.dict()}")  # Debug log
        item_dict = item.dict()
        inserted_id = insert_item_to_db(item_dict)
        return {
            "message": "Food item inserted successfully", 
            "item": item,
            "id": inserted_id
        }
    except HTTPException as e:
        raise e
    except Exception as e:
        print(f"Error in insert_food: {str(e)}")  # Debug log
        raise HTTPException(status_code=500, detail=f"Unexpected error: {str(e)}")

@app.delete("/delete/{item_name}")
def delete_food(item_name: str):
    try:
        deleted = delete_item_from_db(item_name)
        if deleted:
            return {"message": f"{item_name} deleted successfully"}
        else:
            raise HTTPException(status_code=404, detail=f"{item_name} not found")
    except HTTPException as e:
        raise e
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Unexpected error: {str(e)}")