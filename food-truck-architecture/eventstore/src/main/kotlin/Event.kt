package de.muspellheim.portfoliosimulation.eventstore

import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.encoding.*
import java.time.*
import java.util.*

@Serializable
open class Event(
    val id: String = UUID.randomUUID().toString(),

    @Serializable(with = InstantAsLongSerializer::class)
    open val timestamp: Instant = Instant.now()
)

object InstantAsLongSerializer : KSerializer<Instant> {
    override val descriptor = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Instant) = encoder.encodeLong(value.toEpochMilli())
    override fun deserialize(decoder: Decoder): Instant = Instant.ofEpochMilli(decoder.decodeLong())
}
