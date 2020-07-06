package de.muspellheim.portfoliosimulation.contract.messages

interface Message
interface Incoming : Message
interface Outgoing : Message

interface Request : Incoming
interface Response : Outgoing

interface Notification : Incoming, Outgoing

interface Command : Request
interface Query : Request

interface CommandStatus : Response

class Success : CommandStatus

data class Failure(val errorMessage: String) : CommandStatus

interface QueryResult : Response
