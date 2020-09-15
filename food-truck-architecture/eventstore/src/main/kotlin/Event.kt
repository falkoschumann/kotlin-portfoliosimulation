package de.muspellheim.portfoliosimulation.eventstore

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.time.*
import java.util.*

@Serializable
abstract class Event {
    val id: String = UUID.randomUUID().toString()

    @Serializable(with = InstantAsLongSerializer::class)
    abstract val timestamp: Instant
}

object InstantAsLongSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeString(value.toString())
    override fun deserialize(decoder: Decoder): Instant = Instant.parse(decoder.decodeString())
}
