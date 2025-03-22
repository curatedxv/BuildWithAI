from flask import Flask, request, jsonify

app = Flask(__name__)

@app.route('/chat', methods=['POST'])
def chat():
 data = request.get_json()
 message = data.get('message', '')
 
 # Example response logic
 response_message = f"You said: {message}"

 return jsonify({"response": response_message})

if __name__ == '__main__':
 app.run(port=5000, debug=True)