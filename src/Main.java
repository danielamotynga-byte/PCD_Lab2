
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;

public class Main {

    static class MyThread extends Thread {
        private final JTextArea output;

        public MyThread(ThreadGroup group, String name, int priority, JTextArea output) {
            super(group, name);
            setPriority(priority);
            this.output = output;
        }

        @Override
        public void run() {
            String message = getName()
                    + " | Группа: " + getThreadGroup().getName()
                    + " | Приоритет: " + getPriority();

            SwingUtilities.invokeLater(() -> output.append(message + "\n"));
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createWindow);
    }

    static void createWindow() {
        JFrame frame = new JFrame("PCD - Лабораторная №2 - Вариант 1");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        JTextArea output = new JTextArea();
        output.setEditable(false);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Main");
        DefaultMutableTreeNode g1Node = new DefaultMutableTreeNode("G1");
        DefaultMutableTreeNode g2Node = new DefaultMutableTreeNode("G2");
        DefaultMutableTreeNode g3Node = new DefaultMutableTreeNode("G3");

        root.add(g1Node);
        root.add(g2Node);
        g1Node.add(g3Node);

        ThreadGroup mainGroup = new ThreadGroup("Main");
        ThreadGroup g1 = new ThreadGroup(mainGroup, "G1");
        ThreadGroup g2 = new ThreadGroup(mainGroup, "G2");
        ThreadGroup g3 = new ThreadGroup(g1, "G3");

        java.util.List<Thread> threads = new java.util.ArrayList<>();

        addThread(threads, g3, g3Node, "Tha", 3, output);
        addThread(threads, g3, g3Node, "Thb", 3, output);
        addThread(threads, g3, g3Node, "Thc", 3, output);
        addThread(threads, g3, g3Node, "Thd", 3, output);

        addThread(threads, g2, g2Node, "Th1", 4, output);
        addThread(threads, g2, g2Node, "Th2", 5, output);
        addThread(threads, g2, g2Node, "Th3", 5, output);

        addThread(threads, mainGroup, root, "Th1", 7, output);
        addThread(threads, mainGroup, root, "Th2", 7, output);
        addThread(threads, mainGroup, root, "ThA", 3, output);

        JTree tree = new JTree(root);

        JButton button = new JButton("Запустить потоки");
        button.addActionListener(e -> {
            button.setEnabled(false);
            output.setText("");

            for (Thread thread : threads) {
                thread.start();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(button, BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree),
                new JScrollPane(output)
        );

        split.setDividerLocation(300);
        panel.add(split, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    static void addThread(
            java.util.List<Thread> threads,
            ThreadGroup group,
            DefaultMutableTreeNode node,
            String name,
            int priority,
            JTextArea output) {

        Thread thread = new MyThread(group, name, priority, output);
        threads.add(thread);
        node.add(new DefaultMutableTreeNode(name + " (" + priority + ")"));
    }
}
