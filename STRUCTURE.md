USER                                    ADMIN
  │                                       │
  ├─ Connect WS                          ├─ Connect WS
  ├─ Subscribe notifications             ├─ Subscribe admin notifications
  │                                       │
  ├─ Create Room ──────────────────────► │
  │                  [Notification]       │
  ├─ Subscribe to room                   │
  │                                       │
  │                                       ├─ See pending room
  │                                       ├─ Approve room
  │ ◄────────────────────────────────────┤
  │              [Notification]           │
  │                                       ├─ Subscribe to room
  ├─ Subscribe to room (on approval)     │
  │                                       │
  ├─ Send message ─────────────────────► │
  │              [WebSocket]              │
  │                                       ├─ Receive message (real-time)
  │                                       ├─ Reply message
  │ ◄────────────────────────────────────┤
  │              [WebSocket]              │
  ├─ Receive message (real-time)         │


  Plan AI :
  Method	Endpoint	Request
POST	/api/generated-cvs	{userId, templateId, prompt, title?}
GET	/api/generated-cvs/my-cvs?userId=1&page=0&size=10	Query params
GET	/api/generated-cvs/{id}	Path param
PUT	/api/generated-cvs/{id}	{prompt?, title?} (regenerate với prompt mới)
DELETE	/api/generated-cvs/{id}	Path param