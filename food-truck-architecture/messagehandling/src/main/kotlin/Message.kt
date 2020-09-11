package de.muspellheim.portfoliosimulation.messagehandling

interface Message
interface Incoming : Message
interface Outgoing : Message

interface Request : Incoming
interface Response : Outgoing

interface Notification : Incoming, Outgoing

interface Command : Request
interface Query : Request

interface CommandStatus : Response

object Success : CommandStatus

data class Failure(val errorMessage: String) : CommandStatus

interface QueryResult : Response

object NoResponse : Response
