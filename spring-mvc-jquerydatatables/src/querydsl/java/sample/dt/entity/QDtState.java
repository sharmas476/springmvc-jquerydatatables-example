package sample.dt.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QDtState is a Querydsl query type for DtState
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QDtState extends EntityPathBase<DtState> {

    private static final long serialVersionUID = -1761528688L;

    public static final QDtState dtState = new QDtState("dtState");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath tableId = createString("tableId");

    public final ArrayPath<byte[], Byte> tableSettings = createArray("tableSettings", byte[].class);

    public QDtState(String variable) {
        super(DtState.class, forVariable(variable));
    }

    public QDtState(Path<? extends DtState> path) {
        super(path.getType(), path.getMetadata());
    }

    public QDtState(PathMetadata<?> metadata) {
        super(DtState.class, metadata);
    }

}

