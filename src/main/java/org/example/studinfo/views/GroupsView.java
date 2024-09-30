package org.example.studinfo.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.example.studinfo.DTO.GroupDTO;
import org.example.studinfo.entities.Group;
import org.example.studinfo.services.impl.GroupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import javax.annotation.Nullable;

@Route("api/groups")
public class GroupsView extends VerticalLayout {
    private final GroupServiceImpl groupService;

    private Grid<Group> groupGrid;

    private TextField numberField;
    private TextField nameField;

    private HorizontalLayout editButtonsPanel;

    GroupView dialog;

    private Group selectedGroup;

    @Autowired
    public GroupsView(GroupServiceImpl groupService){
        this.groupService = groupService;

        dialog = new GroupView();

        setupEditButtons();
        setupGroupGrid();
        setupGetBackButton();

        this.add(editButtonsPanel, groupGrid);
    }

    private void setupGroupGrid() {
        groupGrid = new Grid<>(Group.class);

        groupGrid.setColumns("number", "name");
        groupGrid.setItems(groupService.findAll());

        groupGrid.addItemClickListener(e -> {
            if (e.getClickCount() == 2) {
                selectedGroup = e.getItem();
                openUpdateDialog(selectedGroup);
            }
        });
    }

    private void setupEditButtons() {
        editButtonsPanel = new HorizontalLayout();

        Button addButton = new Button("Добавить", e -> openAddDialog());
        Button editButton = new Button("Изменить", e -> {
            selectedGroup = groupGrid.asSingleSelect().getValue();
            openUpdateDialog(groupGrid.asSingleSelect().getValue());
        });

        Button deleteButton = new Button("Удалить", e -> {
            selectedGroup = groupGrid.asSingleSelect().getValue();
            deleteGroup(selectedGroup);
        });

        editButtonsPanel.add(addButton, editButton, deleteButton);
    }


    private void setupGetBackButton() {
        Button getBackButton = new Button("Назад", e -> {
            UI.getCurrent().navigate(HomeView.class);
        });

        editButtonsPanel.add(getBackButton);
        editButtonsPanel.setAlignSelf(Alignment.END, getBackButton);
    }

    private void openAddDialog() {
        dialog.setHeaderTitle("Добавить группу");
        dialog.saveButton.addClickListener(e -> saveGroup(null));
        dialog.open();
    }

    private void openUpdateDialog(Group group) {
        if (group != null) {
            numberField.setValue(group.getNumber());
            nameField.setValue(group.getName());

            dialog.setHeaderTitle("Изменить группу");

            dialog.open();
        } else {
            Notification.show("Сначала выберите группу для редактирования.");
        }
    }

    private void saveGroup(@Nullable Group group) {
        if(fieldsInvalid())
            return;

        GroupDTO dto = GroupDTO.builder()
                .name(nameField.getValue())
                .number(numberField.getValue())
                .build();

        if (group == null) {
            groupService.create(dto);
            Notification.show("Группа добавлена");
        }
        else {
            groupService.update(group.getId(), dto);
            Notification.show("Группа изменена");
        }
        groupGrid.setItems(groupService.findAll());
        clearForm();
        dialog.close();
    }

    private void deleteGroup(Group group) {
        if (group != null) {
            try {
                groupService.deleteById(group.getId());
                groupGrid.setItems(groupService.findAll());
                Notification.show("Группа удалена");
            } catch (DataIntegrityViolationException e) {
                Notification.show("Нельзя удалить группу, в которой числятся студенты");
            }
        } else {
            Notification.show("Сначала выберите группу для удаления.");
        }
    }

    private void clearForm() {
        numberField.clear();
        nameField.clear();
        selectedGroup = null;
    }

    private boolean fieldsInvalid(){
        return numberField.isInvalid() || numberField.getValue().isEmpty();
    }

    private class GroupView extends Dialog {
        private final Button saveButton;

        Binder<Group> binder;

        GroupView() {
            saveButton = new Button("Сохранить", e -> saveGroup(selectedGroup));
            Button cancelButton = new Button("Отмена", e -> {
                close();
                clearForm();
            });

            Button deleteButton = new Button("Удалить", e -> {
                deleteGroup(selectedGroup);
                clearForm();
                close();
            });


            HorizontalLayout buttonPanel = new HorizontalLayout(saveButton, cancelButton);

            binder = new Binder<>(Group.class);

            numberField = new TextField("Номер группы");
            nameField = new TextField("Название");
            binder.forField(numberField)
                    .withValidator(name -> name.length() == 12, "Номер группы должен состоять из 12 символов")
                    .withValidator(surname -> !surname.isEmpty(), "Поле не должно быть пустым")
                    .bind(Group::getNumber, Group::setNumber);

            FormLayout formLayout = new FormLayout(numberField, nameField);

            this.add(formLayout, buttonPanel);
        }
    }
}
