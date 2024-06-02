import { SSEAlreadyConnectedException, SSESubscriptionException, SSEUnsubscriptionException } from "./sse-exceptions.js";

export class SSEClient
{
    #baseUrl;
    #clientId;

    #eventSource;

    constructor(baseUrl)
    {
        this.baseUrl = baseUrl;
        this.clientId = this.#loadClientId();

        this.eventSource = null;
    }

    #loadClientId()
    {
        let uuid = sessionStorage.getItem(`sse-id-${this.baseUrl}`);

        if (uuid == null)
        {
            uuid = this.#generateClientId();

            this.#saveClientId(uuid);
        }

        return uuid;
    }

    #saveClientId(clientId)
    {
        sessionStorage.setItem(`sse-id-${this.baseUrl}`, clientId);
    }

    #generateClientId()
    {
        return crypto.randomUUID();
    }

    async connect()
    {
        if (this.eventSource !== null)
            throw new SSEAlreadyConnectedException();

        this.eventSource = new EventSource(`//${this.baseUrl}/__sse/${this.clientId}`, {});
    }

    async disconnect()
    {
        if (this.eventSource)
        {
            this.eventSource.close();
            this.eventSource = null;
        }
    }

    async subscribe(channel, callback)
    {
        if(callback === undefined)
            return;

        const response = await fetch(`//${this.baseUrl}/__sse/${this.clientId}/channel/${channel}`, { method: "post" });

        if (response.status !== 200)
            throw new SSESubscriptionException(channel);

        this.eventSource.addEventListener(channel, (event) =>
        {
            try{
                callback(JSON.parse(event.data));
            }
            catch(e)
            {
                callback({});
            }
        })
    }

    async unsubscribe(channel)
    {
        const response = await fetch(`//${this.baseUrl}/__sse/${this.clientId}/channel/${channel}`, { method: "delete" });

        if (response.status !== 200)
            throw new SSEUnsubscriptionException(channel);
    }
}