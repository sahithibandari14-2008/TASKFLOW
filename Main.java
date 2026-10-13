import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;

public class Main {
    static final Path FILE = Paths.get("tasks.txt");
    static final Path BACKUP = Paths.get("tasks-backup.txt");

    static class Task {
        String name;
        String priority;
        String category;
        String dueDate;
        boolean done;

        Task(String name, String priority, String category, String dueDate, boolean done) {
            this.name = name;
            this.priority = priority;
            this.category = category;
            this.dueDate = dueDate;
            this.done = done;
        }
    }

    static ArrayList<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createApp);
    }

    static void createApp() {
        
JFrame frame = new JFrame("TaskFlow | Smart To-Do Manager");
frame.setSize(1000, 700);
frame.setMinimumSize(new Dimension(900, 620));
frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
frame.setLocationRelativeTo(null);

Color bg = new Color(242, 246, 252);
Color blue = new Color(55, 105, 210);
Color dark = new Color(35, 48, 75);
Color card = Color.WHITE;

frame.getContentPane().setBackground(bg);

        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBackground(bg);
        root.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));

JLabel heading = new JLabel("TaskFlow", SwingConstants.CENTER);
heading.setFont(new Font("Segoe UI", Font.BOLD, 32));
heading.setForeground(new Color(35, 48, 75));


       
JLabel subtitle = new JLabel(
        "PLAN SMARTER. GET THINGS DONE.",
        SwingConstants.CENTER);
subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
subtitle.setForeground(new Color(110, 125, 150));


        JPanel header = new JPanel(new GridLayout(2, 1, 3, 3));
        header.setBackground(bg);
        header.add(heading);
        header.add(subtitle);

        JTextField nameInput = new JTextField();
        nameInput.setFont(new Font("Arial", Font.PLAIN, 14));
        
JTextField searchInput = new JTextField();
searchInput.setToolTipText("Search tasks by name");
searchInput.setPreferredSize(new Dimension(120, 30));
searchInput.setColumns(12);


        String[] priorities = {"High", "Medium", "Low"};
        JComboBox<String> priorityInput = new JComboBox<>(priorities);
        String[] categories = {"College", "Personal", "Projects"};
        JComboBox<String> categoryInput = new JComboBox<>(categories);
        JTextField dateInput = new JTextField(10);
        dateInput.setToolTipText("Enter date as YYYY-MM-DD");

        JButton addButton = new JButton("Add Task");
        style(addButton, blue);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        form.setBackground(bg);
        form.add(new JLabel("Task:"));
        nameInput.setPreferredSize(new Dimension(200, 30));
        form.add(nameInput);
        form.add(new JLabel("Priority:"));
        form.add(priorityInput);
        form.add(new JLabel("Category:"));
        form.add(categoryInput);
        form.add(new JLabel("Due date:"));
        form.add(dateInput);
        form.add(addButton);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        searchPanel.setBackground(bg);
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchInput);

        JButton allButton = new JButton("All Tasks");
        JButton pendingButton = new JButton("Pending");
        JButton completedButton = new JButton("Completed");
        JButton prioritySortButton = new JButton("Sort by Priority");
        JButton dateSortButton = new JButton("Sort by Due Date");
        JButton markButton = new JButton("Mark Completed");
        JButton deleteButton = new JButton("Delete Task");
        JButton editButton = new JButton("Edit Task");

        style(allButton, new Color(80, 95, 120));
        style(pendingButton, new Color(220, 155, 45));
        style(completedButton, new Color(40, 155, 110));
        style(markButton, new Color(40, 155, 110));
        style(deleteButton, new Color(210, 75, 75));
        style(editButton, new Color(100, 180, 220));
        JPanel filters = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 5));
        filters.setBackground(bg);
        filters.add(allButton);
        filters.add(pendingButton);
        filters.add(completedButton);
        filters.add(prioritySortButton);
        filters.add(dateSortButton);
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setFixedCellHeight(32);
        
list.setCellRenderer(new DefaultListCellRenderer() {
    @Override
    public Component getListCellRendererComponent(
            JList<?> list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {

        JLabel label = (JLabel) super.getListCellRendererComponent(
                list, value, index, isSelected, cellHasFocus);

        label.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));

        if (!isSelected) {
            label.setBackground(index % 2 == 0
                    ? Color.WHITE : new Color(245, 248, 253));
            label.setForeground(new Color(45, 55, 75));
        }

        return label;
    }
});


        String[] currentFilter = {"All"};

        
JLabel totalValue = new JLabel("0", SwingConstants.CENTER);
JLabel completedValue = new JLabel("0", SwingConstants.CENTER);
JLabel pendingValue = new JLabel("0", SwingConstants.CENTER);

JLabel totalTitle = new JLabel("TOTAL TASKS", SwingConstants.CENTER);
JLabel completedTitle = new JLabel("COMPLETED", SwingConstants.CENTER);
JLabel pendingTitle = new JLabel("PENDING", SwingConstants.CENTER);

JPanel statsPanel = new JPanel(new GridLayout(1, 3, 16, 0));
statsPanel.setBackground(bg);
statsPanel.setBorder(
        BorderFactory.createEmptyBorder(8, 0, 8, 0));

JPanel totalCard = new JPanel(new GridLayout(2, 1, 4, 4));
JPanel completedCard = new JPanel(new GridLayout(2, 1, 4, 4));
JPanel pendingCard = new JPanel(new GridLayout(2, 1, 4, 4));

JPanel[] cards = {totalCard, completedCard, pendingCard};
JLabel[] values = {totalValue, completedValue, pendingValue};
JLabel[] titles = {totalTitle, completedTitle, pendingTitle};
Color[] accents = {
    new Color(55, 105, 210),
    new Color(35, 155, 105),
    new Color(225, 155, 45)
};

for (int i = 0; i < cards.length; i++) {
    cards[i].setBackground(Color.WHITE);
    cards[i].setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(
                    0, 4, 0, 0, accents[i]),
            BorderFactory.createEmptyBorder(12, 8, 12, 8)));

    values[i].setFont(new Font("Segoe UI", Font.BOLD, 26));
    values[i].setForeground(accents[i]);

    titles[i].setFont(new Font("Segoe UI", Font.BOLD, 11));
    titles[i].setForeground(new Color(110, 125, 150));

    cards[i].add(titles[i]);
    cards[i].add(values[i]);
    statsPanel.add(cards[i]);
}


        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(bg);
        top.add(form, BorderLayout.NORTH);
        top.add(searchPanel, BorderLayout.CENTER);
        top.add(filters, BorderLayout.SOUTH);

        
JPanel actions = new JPanel();
actions.setBackground(bg);
actions.add(markButton);
actions.add(editButton);
actions.add(deleteButton);



        JPanel center = new JPanel(new BorderLayout(8, 8));
        center.setBackground(bg);
        center.add(top, BorderLayout.NORTH);
        center.add(new JScrollPane(list), BorderLayout.CENTER);
        center.add(actions, BorderLayout.SOUTH);

        root.add(header, BorderLayout.NORTH);
        root.add(center, BorderLayout.CENTER);
       root.add(statsPanel, BorderLayout.SOUTH);

        // Load tasks, supporting the previous [DONE] format.
        loadTasks(frame);

        Runnable save = () -> {
            try {
                if (Files.exists(FILE) && !Files.exists(BACKUP)) {
                    Files.copy(FILE, BACKUP);
                }

                try (BufferedWriter writer = Files.newBufferedWriter(FILE)) {
                    for (Task t : tasks) {
                        
String encodedName = Base64.getEncoder().encodeToString(
        t.name.getBytes(java.nio.charset.StandardCharsets.UTF_8));

String encodedCategory = Base64.getEncoder().encodeToString(
        t.category.getBytes(java.nio.charset.StandardCharsets.UTF_8));

writer.write((t.done ? "1" : "0") + "|"
        + t.priority + "|" + t.dueDate + "|"
        + encodedCategory + "|" + encodedName);

                        writer.newLine();
                    }
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(frame, "Could not save tasks.");
            }
        };

        Runnable refresh = () -> {
            model.clear();
            int doneCount = 0;

            for (Task t : tasks) {
                if (t.done) doneCount++;

               
if ((currentFilter[0].equals("All")
        || (currentFilter[0].equals("Pending") && !t.done)
        || (currentFilter[0].equals("Completed") && t.done))
        && t.name.toLowerCase().contains(
                searchInput.getText().trim().toLowerCase())) {

                    String status = t.done ? "[DONE] " : "";
String deadline = "";

if (!t.done && !t.dueDate.equals("No date")) {
    LocalDate due = LocalDate.parse(t.dueDate);

    if (due.isBefore(LocalDate.now())) {
        deadline = " | OVERDUE";
    }
}

model.addElement(status + t.name + " | "
        + t.priority + " priority | "
        + t.category + " | Due: "
        + t.dueDate + deadline);
                }
            }

            totalValue.setText(String.valueOf(tasks.size()));
            completedValue.setText(String.valueOf(doneCount));
            pendingValue.setText(String.valueOf(tasks.size() - doneCount));
        };

        allButton.addActionListener(e -> {
            currentFilter[0] = "All";
            refresh.run();
        });

        pendingButton.addActionListener(e -> {
            currentFilter[0] = "Pending";
            refresh.run();
        });

        completedButton.addActionListener(e -> {
            currentFilter[0] = "Completed";
            refresh.run();
        });

        addButton.addActionListener(e -> {
            String name = nameInput.getText().trim();
            String date = dateInput.getText().trim();
            String priority = (String) priorityInput.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Enter a task name.");
                return;
            }

            if (!date.isEmpty()) {
                try {
                    LocalDate.parse(date);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Enter the due date as YYYY-MM-DD.");
                    return;
                }
            } else {
                date = "No date";
            }

            String category = (String) categoryInput.getSelectedItem();
            tasks.add(new Task(name, priority, category, date, false));
            nameInput.setText("");
            dateInput.setText("");
            save.run();
            refresh.run();
        });

        nameInput.addActionListener(e -> addButton.doClick());
        
searchInput.getDocument().addDocumentListener(
    new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            refresh.run();
        }

        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            refresh.run();
        }

        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            refresh.run();
        }
    }
);



        markButton.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(frame, "Select a task first.");
                return;
            }

            Task selected = getVisibleTask(index, currentFilter[0], searchInput.getText());
            if (selected != null) {
                selected.done = !selected.done;
                save.run();
                refresh.run();
            }
        });

        deleteButton.addActionListener(e -> {
            int index = list.getSelectedIndex();
            if (index < 0) {
                JOptionPane.showMessageDialog(frame, "Select a task first.");
                return;
            }

            Task selected = getVisibleTask(index, currentFilter[0], searchInput.getText());
            if (selected != null) {
                tasks.remove(selected);
                save.run();
                refresh.run();
            }
        });

        
editButton.addActionListener(e -> {
    int index = list.getSelectedIndex();

    if (index < 0) {
        JOptionPane.showMessageDialog(frame, "Select a task to edit.");
        return;
    }

    Task selected = getVisibleTask(index, currentFilter[0], searchInput.getText());

    if (selected == null) return;

    JTextField editName = new JTextField(selected.name);
    JComboBox<String> editPriority = new JComboBox<>(
            new String[]{"High", "Medium", "Low"});
    editPriority.setSelectedItem(selected.priority);
    JComboBox<String> editCategory = new JComboBox<>(
        new String[]{"College", "Personal", "Projects"});
    editCategory.setSelectedItem(selected.category);

    JTextField editDate = new JTextField(
            selected.dueDate.equals("No date") ? "" : selected.dueDate);

    Object[] fields = {
        "Task name:", editName,
        "Priority:", editPriority,
        "Category:", editCategory,
        "Due date (YYYY-MM-DD):", editDate
    };

    int choice = JOptionPane.showConfirmDialog(
            frame, fields, "Edit Task",
            JOptionPane.OK_CANCEL_OPTION);

    if (choice != JOptionPane.OK_OPTION) return;

    String name = editName.getText().trim();
    String date = editDate.getText().trim();

    if (name.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Task name cannot be empty.");
        return;
    }

    if (!date.isEmpty()) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(
                    frame, "Enter the due date as YYYY-MM-DD.");
            return;
        }
    } else {
        date = "No date";
    }

    selected.name = name;
    selected.priority = (String) editPriority.getSelectedItem();
    selected.category = (String) editCategory.getSelectedItem();
    selected.dueDate = date;

    save.run();
    refresh.run();
});

        
        prioritySortButton.addActionListener(e -> {
    tasks.sort((a, b) -> {
        int p1 = a.priority.equals("High") ? 1
                : a.priority.equals("Medium") ? 2 : 3;
        int p2 = b.priority.equals("High") ? 1
                : b.priority.equals("Medium") ? 2 : 3;
        return Integer.compare(p1, p2);
    });
    save.run();
    refresh.run();
});

dateSortButton.addActionListener(e -> {
    tasks.sort((a, b) -> {
        if (a.dueDate.equals("No date")) return 1;
        if (b.dueDate.equals("No date")) return -1;
        return a.dueDate.compareTo(b.dueDate);
    });
    save.run();
    refresh.run();
});

        frame.setContentPane(root);
        refresh.run();
        frame.setVisible(true);
    }


        
static Task getVisibleTask(int index, String filter, String search) {
    int visibleIndex = 0;

    for (Task t : tasks) {
        boolean matchesFilter = filter.equals("All")
                || (filter.equals("Pending") && !t.done)
                || (filter.equals("Completed") && t.done);

        boolean matchesSearch = t.name.toLowerCase()
                .contains(search.trim().toLowerCase());

        if (matchesFilter && matchesSearch) {
            if (visibleIndex == index) {
                return t;
            }
            visibleIndex++;
        }
    }

    return null;
}

    static void loadTasks(JFrame frame) {
        if (!Files.exists(FILE)) return;

        try {
            for (String line : Files.readAllLines(FILE)) {
                if (line.isEmpty()) continue;     
if (line.contains("|")) {
    String[] parts = line.split("\\|", 5);

    if (parts.length == 5) {
        String category = new String(
                Base64.getDecoder().decode(parts[3]),
                java.nio.charset.StandardCharsets.UTF_8);

        String name = new String(
                Base64.getDecoder().decode(parts[4]),
                java.nio.charset.StandardCharsets.UTF_8);

        tasks.add(new Task(name, parts[1], category,
                parts[2], parts[0].equals("1")));

    } else if (parts.length == 4) {
        String name = new String(
                Base64.getDecoder().decode(parts[3]),
                java.nio.charset.StandardCharsets.UTF_8);

        tasks.add(new Task(name, parts[1], "College",
                parts[2], parts[0].equals("1")));
    }

} else {
    boolean done = line.startsWith("[DONE] ");
    String name = done ? line.substring(7) : line;

    tasks.add(new Task(name, "Medium", "College",
            "No date", done));
}
            }
            }  
         catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(frame,
                    "Could not read tasks. Check tasks.txt and tasks-backup.txt.");
        }
    }

    static void style(JButton button, Color color) {
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false); 
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
    }
}