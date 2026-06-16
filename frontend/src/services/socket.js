import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const socketUrl =
  process.env.REACT_APP_WS_URL ||
  "http://localhost:8080/ws";

const stompClient = new Client({
  webSocketFactory: () => new SockJS(socketUrl),
  reconnectDelay: 5000,
});

export default stompClient;