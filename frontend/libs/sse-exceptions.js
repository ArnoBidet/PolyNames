export class SSESubscriptionException extends Error
{
    constructor(channel)
    {
        super(`Unable to subscribe to '${channel}' channel`);
    }
}

export class SSEUnsubscriptionException extends Error
{
    constructor(channel)
    {
        super(`Unable to unsubscribe from '${channel}' channel`);
    }
}

export class SSEAlreadyConnectedException extends Error
{
    constructor()
    {
        super(`Connection is already open`);
    }
}