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