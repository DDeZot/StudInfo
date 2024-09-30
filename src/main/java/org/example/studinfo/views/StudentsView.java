package org.example.studinfo.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import org.example.studinfo.DTO.StudentDTO;
import org.example.studinfo.entities.Group;
import org.example.studinfo.entities.Student;
import org.example.studinfo.services.impl.GroupServiceImpl;
import org.example.studinfo.services.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.time.LocalDate;
import java.util.Objects;

@Route("api/students")
public class StudentsView extends VerticalLayout {
    private final StudentServiceImpl studentService;
    private final GroupServiceImpl groupService;

    private Grid<Student> studentGrid;

    private HorizontalLayout editButtonsPanel;

    private TextField surnameField;
    private TextField nameField;
    private TextField patronymicField;
    private DatePicker birthDateField;
    private Select<Group> groupField;

    private TextField filterSurname;
    private Select<Group> filterGroup;

    private StudentView dialog;

    Student selectedStudent;

    @Autowired
    public StudentsView(StudentServiceImpl studentService, GroupServiceImpl groupService) {
        this.studentService = studentService;
        this.groupService = groupService;

        dialog = new StudentView();

        setupStudentGrid();
        setupFilterPanel();
        setupEditButtons();
        setupGetBackButton();

        this.add(editButtonsPanel, studentGrid);
    }

    private void setupStudentGrid() {
        studentGrid = new Grid<>(Student.class);

        studentGrid.setColumns("surname", "name", "patronymic", "birthDate", "groupNumber");
        studentGrid.setItems(studentService.findAll());

        studentGrid.addItemClickListener(e -> {
            if (e.getClickCount() == 2) {
                selectedStudent = e.getItem();
                openUpdateDialog(selectedStudent);
            }
        });
    }

    private void setupGetBackButton() {
        Button getBackButton = new Button("Назад", e -> {
            UI.getCurrent().navigate(HomeView.class);
        });

        editButtonsPanel.add(getBackButton);
    }

    private void setupEditButtons() {
        editButtonsPanel = new HorizontalLayout();

        Button addButton = new Button("Добавить", e -> openAddDialog());
        Button editButton = new Button("Изменить", e -> {
            selectedStudent = studentGrid.asSingleSelect().getValue();
            openUpdateDialog(selectedStudent);
        });

        Button deleteButton = new Button("Удалить", e -> {
            selectedStudent = studentGrid.asSingleSelect().getValue();
            deleteStudent(selectedStudent);
        });

        editButtonsPanel.add(addButton, editButton, deleteButton);
    }

    void setupFilterPanel() {
        filterSurname = new TextField("Фильтровать по фамилии");

        filterGroup = new Select<>();
        filterGroup.setItems(groupService.findAll());
        filterGroup.setItemLabelGenerator(g -> {
            if(g == null)
                return "<None>";
            else
                return g.getNumber();
        });

        filterGroup.setLabel("Фильтровать по группе");
        filterGroup.setEmptySelectionAllowed(true);
        filterGroup.setEmptySelectionCaption("<None>");

        Button applyFiltersButton = new Button("Применить", e -> applyFilters());

        HeaderRow filterRow = studentGrid.appendHeaderRow();

        filterRow.getCell(studentGrid.getColumnByKey("surname")).setComponent(filterSurname);
        filterRow.getCell(studentGrid.getColumnByKey("groupNumber")).setComponent(
                new HorizontalLayout(filterGroup, applyFiltersButton));
    }

    private void openAddDialog() {
        dialog.setHeaderTitle("Добавить студента");
        dialog.saveButton.addClickListener(e -> saveStudent(null));
        dialog.open();
    }

    private void openUpdateDialog(Student student) {
        if (student != null) {
            surnameField.setValue(student.getSurname());
            nameField.setValue(student.getName());
            patronymicField.setValue(student.getPatronymic());
            birthDateField.setValue(student.getBirthDate());
            groupField.setValue(student.getGroup());

            dialog.setHeaderTitle("Изменить студента");

            dialog.open();
        } else {
            Notification.show("Сначала выберите студента для редактирования.");
        }
    }

    private void saveStudent(@Nullable Student student) {
        if(fieldsInvalid())
            return;

        StudentDTO dto = StudentDTO.builder()
                .name(nameField.getValue())
                .surname(surnameField.getValue())
                .patronymic(patronymicField.getValue())
                .birthDate(birthDateField.getValue())
                .group(groupField.getValue())
                .build();

        if (student == null) {
            studentService.create(dto);
            Notification.show("Студент добавлен");
        }
        else {
            studentService.update(student.getId(), dto);
            Notification.show("Студент изменён");
        }
        studentGrid.setItems(studentService.findAll());
        clearForm();
        dialog.close();
    }

    private void deleteStudent(Student student) {
        if (student != null) {
            studentService.deleteById(student.getId());
            studentGrid.setItems(studentService.findAll());
            Notification.show("Студент удалён");
        } else {
            Notification.show("Сначала выберите студента для удаления.");
        }
    }

    private void applyFilters(){
        if(filterSurname.isEmpty() && !filterGroup.isEmpty())
            studentGrid.setItems(studentService.findAllByGroupNumber(filterGroup.getValue().getNumber()));
        else if(filterGroup.isEmpty() && !filterSurname.isEmpty())
            studentGrid.setItems(studentService.findAllBySurname(filterSurname.getValue()));
        else if(!filterGroup.isEmpty() && !filterSurname.isEmpty())
            studentGrid.setItems(studentService.findAllBySurnameAndGroupNumber(filterSurname.getValue(), filterGroup.getValue().getNumber()));
        else if (filterSurname.isEmpty() && filterGroup.isEmpty())
            studentGrid.setItems(studentService.findAll());
    }

    private boolean fieldsInvalid(){
        return (surnameField.isInvalid()
                || nameField.isInvalid()
                || patronymicField.isInvalid()
                || birthDateField.isInvalid())
                || (surnameField.getValue().isEmpty()
                || nameField.getValue().isEmpty()
                || birthDateField.getValue() == null);
    }

    private void clearForm() {
        surnameField.clear();
        nameField.clear();
        patronymicField.clear();
        birthDateField.clear();
        groupField.clear();
        selectedStudent = null;
    }

    private class StudentView extends Dialog {
        private final Button saveButton;

        Binder<Student> binder;

        StudentView() {
            saveButton = new Button("Сохранить", e -> saveStudent(selectedStudent));
            Button cancelButton = new Button("Отмена", e -> {
                close();
                clearForm();
            });

            Button deleteButton = new Button("Удалить", e -> {
                deleteStudent(selectedStudent);
                clearForm();
                close();
            });


            HorizontalLayout buttonPanel = new HorizontalLayout(saveButton, cancelButton);

            binder = new Binder<>(Student.class);
            surnameField = new TextField("Фамилия");
            binder.forField(surnameField)
                    .withValidator(surname -> surname.length() <= 60, "Не более 60 символов")
                    .withValidator(surname -> !surname.isEmpty(), "Поле не должно быть пустым")
                    .bind(Student::getSurname, Student::setSurname);

            nameField = new TextField("Имя");
            binder.forField(nameField)
                    .withValidator(name -> name.length() <= 60, "Не более 60 символов")
                    .withValidator(name -> !name.isEmpty(), "Поле не должно быть пустым")
                    .bind(Student::getName, Student::setName);

            patronymicField = new TextField("Отчество");
            binder.forField(patronymicField)
                    .withValidator(patronymic -> patronymic.length() <= 60, "Не более 60 символов")
                    .bind(Student::getPatronymic, Student::setPatronymic);

            birthDateField = new DatePicker("Дата рождения");
            binder.forField(birthDateField)
                    .withValidator(birthDate -> LocalDate.now().minusYears(birthDate.getYear()).getYear() >= 17, "Студент не может быть младше 17 лет")
                    .withValidator(Objects::nonNull, "Поле не должно быть пустым")
                    .bind(Student::getBirthDate, Student::setBirthDate);

            groupField = new Select<>();
            groupField.setLabel("Номер группы");
            groupField.setItems(groupService.findAll());
            groupField.setItemLabelGenerator(Group::getNumber);

            FormLayout formLayout = new FormLayout(surnameField, birthDateField, nameField, groupField, patronymicField);
            formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("500px", 2));

            this.add(formLayout, buttonPanel);
        }
    }
}
