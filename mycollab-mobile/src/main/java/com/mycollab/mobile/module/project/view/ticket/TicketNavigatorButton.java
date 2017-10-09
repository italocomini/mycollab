package com.mycollab.mobile.module.project.view.ticket;

import com.mycollab.db.arguments.NumberSearchField;
import com.mycollab.module.project.CurrentProjectVariables;
import com.mycollab.module.project.domain.criteria.ProjectTicketSearchCriteria;
import com.mycollab.module.project.i18n.TicketI18nEnum;
import com.mycollab.module.project.service.ProjectTicketService;
import com.mycollab.spring.AppContextUtil;
import com.mycollab.vaadin.UserUIContext;
import com.vaadin.addon.touchkit.ui.NavigationButton;

/**
 * @author MyCollab Ltd
 * @since 5.2.5
 */
public class TicketNavigatorButton extends NavigationButton {
    private Integer milestoneId;

    public TicketNavigatorButton() {
        super(UserUIContext.getMessage(TicketI18nEnum.OPT_TICKETS_VALUE, 0));
        this.addClickListener(navigationButtonClickEvent -> {
            if (milestoneId != null) {
                getNavigationManager().navigateTo(new TicketListDisplayView(milestoneId));
            }
        });
    }

    public void displayTotalIssues(Integer milestoneId) {
        this.milestoneId = milestoneId;
        ProjectTicketSearchCriteria criteria = new ProjectTicketSearchCriteria();
        criteria.setMilestoneId(NumberSearchField.equal(milestoneId));
        criteria.setTypes(CurrentProjectVariables.getRestrictedTicketTypes());
        ProjectTicketService ticketService = AppContextUtil.getSpringBean(ProjectTicketService.class);
        this.setCaption(UserUIContext.getMessage(TicketI18nEnum.OPT_TICKETS_VALUE, ticketService.getTotalCount(criteria)));
    }
}