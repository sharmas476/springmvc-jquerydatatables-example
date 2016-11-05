package sample.dt.entity;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QCompanyUser is a Querydsl query type for CompanyUser
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QCompanyUser extends EntityPathBase<CompanyUser> {

    private static final long serialVersionUID = 525613335L;

    public static final QCompanyUser companyUser = new QCompanyUser("companyUser");

    public final NumberPath<Integer> age = createNumber("age", Integer.class);

    public final StringPath city = createString("city");

    public final DatePath<java.sql.Date> hdate = createDate("hdate", java.sql.Date.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> salary = createNumber("salary", Integer.class);

    public final StringPath uname = createString("uname");

    public final StringPath urole = createString("urole");

    public QCompanyUser(String variable) {
        super(CompanyUser.class, forVariable(variable));
    }

    public QCompanyUser(Path<? extends CompanyUser> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCompanyUser(PathMetadata<?> metadata) {
        super(CompanyUser.class, metadata);
    }

}

