package de.muspellheim.portfoliosimulation.contract.data.messages.commands

interface CommandStatus

class Success : CommandStatus

data class Failure(val errorMessage: String) : CommandStatus
