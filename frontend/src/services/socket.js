import SockJS from "sockjs-client";

import { Client } from "@stomp/stompjs";

const socket = new SockJS(
  process.env.REACT_APP_WS_URL ||
   "http://localhost:8080/ws"
);

const stompClient = new Client({

  webSocketFactory: () => socket,

  reconnectDelay: 5000,
});

export default stompClient;