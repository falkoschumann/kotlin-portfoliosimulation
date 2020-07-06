package de.muspellheim.portfoliosimulation.contract.messages

interface MessageHandling<I : Incoming>

interface RequestHandling<I : Request, O : Response> : MessageHandling<I> {
    fun handle(request: I): O
}

interface CommandHandling<C : Command> : RequestHandling<C, CommandStatus>

interface QueryHandling<Q : Query, R : QueryResult> : RequestHandling<Q, R>

interface NotificationHandling<N : Notification> : MessageHandling<N> {
    fun handle(notification: N)
}
