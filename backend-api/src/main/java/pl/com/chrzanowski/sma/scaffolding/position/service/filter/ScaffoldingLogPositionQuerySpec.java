package pl.com.chrzanowski.sma.scaffolding.position.service.filter;


import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.blazebit.persistence.querydsl.BlazeJPAQueryFactory;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import pl.com.chrzanowski.sma.scaffolding.position.model.ScaffoldingLogPosition;

import static pl.com.chrzanowski.sma.scaffolding.position.model.QScaffoldingLogPosition.scaffoldingLogPosition;

@Component
public class ScaffoldingLogPositionQuerySpec {

    private final BlazeJPAQueryFactory queryFactory;

    public ScaffoldingLogPositionQuerySpec(BlazeJPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    public static BooleanBuilder buildPredicate(ScaffoldingLogPositionFilter filter) {
        BooleanBuilder predicate = new BooleanBuilder();
        if (filter != null) {
            if (filter.getId() != null) {
                predicate.and(scaffoldingLogPosition.id.eq(filter.getId()));
            }
            if (filter.getScaffoldingNumberContains() != null && !filter.getScaffoldingNumberContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.scaffoldingNumber.containsIgnoreCase(filter.getScaffoldingNumberContains()));
            }
            if (filter.getAssemblyLocationContains() != null && !filter.getAssemblyLocationContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.assemblyLocation.containsIgnoreCase(filter.getAssemblyLocationContains()));
            }
            if (filter.getAssemblyDateGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.assemblyDate.goe(filter.getAssemblyDateGreaterOrEqual()));
            }
            if (filter.getAssemblyDateLessOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.assemblyDate.loe(filter.getAssemblyDateLessOrEqual()));
            }
            if (filter.getDismantlingDateGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.dismantlingDate.goe(filter.getDismantlingDateGreaterOrEqual()));
            }
            if (filter.getDismantlingDateLessOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.dismantlingDate.loe(filter.getDismantlingDateLessOrEqual()));
            }
            if (filter.getDismantlingNotificationDateGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.dismantlingNotificationDate.goe(filter.getDismantlingNotificationDateGreaterOrEqual()));
            }
            if (filter.getDismantlingNotificationDateLessOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.dismantlingNotificationDate.loe(filter.getDismantlingNotificationDateLessOrEqual()));
            }
            if (filter.getTechnicalProtocolStatus() != null) {
                predicate.and(scaffoldingLogPosition.technicalProtocolStatus.eq(filter.getTechnicalProtocolStatus()));
            }
            if (filter.getScaffoldingType() != null) {
                predicate.and(scaffoldingLogPosition.scaffoldingType.eq(filter.getScaffoldingType()));
            }
            if (filter.getScaffoldingFullDimensionGreaterOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.scaffoldingFullDimension.goe(filter.getScaffoldingFullDimensionGreaterOrEqual()));
            }
            if (filter.getScaffoldingFullDimensionLessOrEqual() != null) {
                predicate.and(scaffoldingLogPosition.scaffoldingFullDimension.loe(filter.getScaffoldingFullDimensionLessOrEqual()));
            }
            if (filter.getScaffoldingLogNameContains() != null && !filter.getScaffoldingLogNameContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.scaffoldingLog.name.containsIgnoreCase(filter.getScaffoldingLogNameContains()));
            }
            if (filter.getScaffoldingLogAdditionalInfoContains() != null && !filter.getScaffoldingLogAdditionalInfoContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.scaffoldingLog.additionalInfo.containsIgnoreCase(filter.getScaffoldingLogAdditionalInfoContains()));
            }
            if (filter.getContractorNameContains() != null && !filter.getContractorNameContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.contractor.name.containsIgnoreCase(filter.getContractorNameContains()));
            }
            if (filter.getContractorContactNameContains() != null && !filter.getContractorContactNameContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.contractorContact.lastName.containsIgnoreCase(filter.getContractorContactNameContains()));
            }
            if (filter.getScaffoldingUserNameContains() != null && !filter.getScaffoldingUserNameContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.scaffoldingUser.name.containsIgnoreCase(filter.getScaffoldingUserNameContains()));
            }
            if (filter.getScaffoldingUserContactNameContains() != null && !filter.getScaffoldingUserContactNameContains().isEmpty()) {
                predicate.and(scaffoldingLogPosition.scaffoldingUserContact.lastName.containsIgnoreCase(filter.getScaffoldingUserContactNameContains()));
            }
        }
        return predicate;
    }

    public BlazeJPAQuery<ScaffoldingLogPosition> buildQuery(BooleanBuilder builder, Pageable pageable) {
        BlazeJPAQuery<ScaffoldingLogPosition> query = queryFactory
                .selectFrom(scaffoldingLogPosition)
                .where(builder);

        if (pageable != null && pageable.getSort().isSorted()) {
            Sort sort = pageable.getSort();
            sort.forEach(order -> {
                switch (order.getProperty()) {
                    case "id":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.id.asc() : scaffoldingLogPosition.id.desc());
                        break;
                    case "scaffoldingNumber":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingNumber.asc() : scaffoldingLogPosition.scaffoldingNumber.desc());
                        break;
                    case "assemblyLocation":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.assemblyLocation.asc() : scaffoldingLogPosition.assemblyLocation.desc());
                        break;
                    case "assemblyDate":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.assemblyDate.asc() : scaffoldingLogPosition.assemblyDate.desc());
                        break;
                    case "dismantlingDate":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.dismantlingDate.asc() : scaffoldingLogPosition.dismantlingDate.desc());
                        break;
                    case "dismantlingNotificationDate":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.dismantlingNotificationDate.asc() : scaffoldingLogPosition.dismantlingNotificationDate.desc());
                        break;
                    case "scaffoldingType":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingType.asc() : scaffoldingLogPosition.scaffoldingType.desc());
                        break;
                    case "scaffoldingFullDimension":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingFullDimension.asc() : scaffoldingLogPosition.scaffoldingFullDimension.desc());
                        break;
                    case "technicalProtocolStatus":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.technicalProtocolStatus.asc() : scaffoldingLogPosition.technicalProtocolStatus.desc());
                        break;
                    case "scaffoldingLogName":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingLog.name.asc() : scaffoldingLogPosition.scaffoldingLog.name.desc());
                        break;
                    case "contractorName":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.contractor.name.asc() : scaffoldingLogPosition.contractor.name.desc());
                        break;
                    case "contractorContactName":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.contractorContact.lastName.asc() : scaffoldingLogPosition.contractorContact.lastName.desc());
                        break;
                    case "scaffoldingUserName":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingUser.name.asc() : scaffoldingLogPosition.scaffoldingUser.name.desc());
                        break;
                    case "scaffoldingUserContactName":
                        query.orderBy(order.isAscending() ? scaffoldingLogPosition.scaffoldingUserContact.lastName.asc() : scaffoldingLogPosition.scaffoldingUserContact.lastName.desc());
                        break;
                }
            });
            if (sort.stream().noneMatch(order -> "id".equals(order.getProperty()))) {
                query.orderBy(scaffoldingLogPosition.id.asc());
            }
        } else {
            query.orderBy(scaffoldingLogPosition.id.asc());
        }
        return query;
    }
}


