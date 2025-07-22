package tlt.th.co.toyotaleasing.model.entity



open class SequenceTransactionEntity(
        var sequenceId: String = "")  {

    fun transformToRealm(sequence: SequenceTransactionEntity) {
        this@SequenceTransactionEntity.sequenceId = sequence.sequenceId
    }

    fun transform(): SequenceTransactionEntity {
        return SequenceTransactionEntity().apply {
            this.sequenceId = this@SequenceTransactionEntity.sequenceId
        }
    }
}