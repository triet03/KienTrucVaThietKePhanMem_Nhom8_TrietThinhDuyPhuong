from flask import Flask, request, jsonify
from sentence_transformers import SentenceTransformer

app = Flask(__name__)
model = SentenceTransformer('all-MiniLM-L6-v2')

@app.route("/embed", methods=["POST"])
def embed():
    data = request.json
    texts = data.get("texts", [])
    if not texts:
        return jsonify({"error": "No input texts"}), 400

    vectors = model.encode(texts).tolist()
    return jsonify(vectors)

if __name__ == "__main__":
    app.run(port=5001)
