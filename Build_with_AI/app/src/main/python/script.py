from flask import Flask, request, jsonify
from gemini import get_llm_response_gemini
app = Flask(__name__)

@app.route('/chat', methods=['POST'])
def chat():
 data = request.get_json()
 message = data.get('message', '')
 
 # Example response logic
 response_message = get_llm_response_gemini("This is an URL. Give me your best guess in detailed explanation if the content is fake or not:" + message)
 

 return jsonify({"response": response_message})

if __name__ == '__main__':
 app.run(port=5000, debug=True)