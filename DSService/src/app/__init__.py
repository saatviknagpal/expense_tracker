from flask import Flask
from flask import request, jsonify
from app.service.messageService import MessageService

app = Flask(__name__)

messageService = MessageService()

@app.route('/v1/ds/message', methods=['POST'])
def message():
    data = request.json.get('message')
    result = messageService.processMessage(data)
    return jsonify(result)

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8000, debug=True)

