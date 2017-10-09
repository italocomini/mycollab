package com.mycollab.module.project.view.ticket;

import com.mycollab.common.i18n.GenericI18Enum;
import com.mycollab.core.utils.DateTimeUtils;
import com.mycollab.db.arguments.DateSearchField;
import com.mycollab.db.arguments.SearchField;
import com.mycollab.db.arguments.SetSearchField;
import com.mycollab.db.arguments.StringSearchField;
import com.mycollab.module.project.domain.ProjectTicket;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.ProjectI18nEnum;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.mycollab.vaadin.web.ui.DefaultBeanPagedList;
import com.mycollab.vaadin.web.ui.Depot;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CssLayout;

import java.util.List;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
public class TicketOverdueWidget extends Depot {
    private static final long serialVersionUID = 1L;

    private TicketOverduePagedList ticketOverdueComponent;
    private ProjectTicketSearchCriteria searchCriteria;

    public TicketOverdueWidget() {
        super(UserUIContext.getMessage(TicketI18nEnum.VAL_OVERDUE_TICKETS) + " (0)", new CssLayout());

        final CheckBox myItemsOnly = new CheckBox(UserUIContext.getMessage(GenericI18Enum.OPT_MY_ITEMS));
        myItemsOnly.addValueChangeListener(valueChangeEvent -> {
            if (searchCriteria != null) {
                boolean selectMyItemsOnly = myItemsOnly.getValue();
                if (selectMyItemsOnly) {
                    searchCriteria.setAssignUser(StringSearchField.and(UserUIContext.getUsername()));
                } else {
                    searchCriteria.setAssignUser(null);
                }
                ticketOverdueComponent.setSearchCriteria(searchCriteria);
            }
        });

        this.addHeaderElement(myItemsOnly);

        ticketOverdueComponent = new TicketOverduePagedList();
        bodyContent.addComponent(ticketOverdueComponent);
    }

    public void showTicketsByStatus(List<Integer> prjKeys) {
        searchCriteria = new ProjectTicketSearchCriteria();
        searchCriteria.setProjectIds(new SetSearchField<>(prjKeys.toArray(new Integer[prjKeys.size()])));
        searchCriteria.setIsOpenned(new SearchField());
        searchCriteria.setDueDate(new DateSearchField(DateTimeUtils.getCurrentDateWithoutMS()));
        updateSearchResult();
    }

    private void updateSearchResult() {
        ticketOverdueComponent.setSearchCriteria(searchCriteria);
        setTitle(String.format("%s (%d)", UserUIContext.getMessage(TicketI18nEnum.VAL_OVERDUE_TICKETS), ticketOverdueComponent.getTotalCount()));
    }

    private static class TicketOverduePagedList extends DefaultBeanPagedList<ProjectTicketService, ProjectTicketSearchCriteria, ProjectTicket> {

        TicketOverduePagedList() {
            super(AppContextUtil.getSpringBean(ProjectTicketService.class), new TicketRowDisplayHandler(true), 10);
        }

        @Override
        protected String stringWhenEmptyList() {
            return UserUIContext.getMessage(ProjectI18nEnum.OPT_NO_OVERDUE_TICKET);
        }
    }
}