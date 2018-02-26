package ind.xwm.imooc.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;


public class Flipper extends JFrame implements ActionListener {
    private static Logger logger = LogManager.getLogger(Flipper.class);
    private final GridBagConstraints constraints;
    private final JTextField headsText, totalText, devText;
    private final Border border = BorderFactory.createLoweredBevelBorder();
    private final JButton startButton, stopButton;
    private FlipTask flipTask;

    private JTextField makeText() {
        JTextField t = new JTextField(20);
        t.setEditable(false);
        t.setHorizontalAlignment(JTextField.RIGHT);
        t.setBorder(border);
        getContentPane().add(t, constraints);
        return t;
    }

    private JButton makeButton(String caption) {
        JButton b = new JButton(caption);
        b.setActionCommand(caption);
        b.addActionListener(this);
        getContentPane().add(b, constraints);
        return b;
    }

    public Flipper() {
        super("Flipper");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Make text boxes
        getContentPane().setLayout(new GridBagLayout());
        constraints = new GridBagConstraints();
        constraints.insets = new Insets(3, 10, 3, 10);
        headsText = makeText();
        totalText = makeText();
        devText = makeText();

        //Make buttons
        startButton = makeButton("Start");
        stopButton = makeButton("Stop");
        stopButton.setEnabled(false);

        //Display the window.
        pack();
        setVisible(true);
    }


    private static class FlipPair {
        private final long heads, total;

        FlipPair(long heads, long total) {
            this.heads = heads;
            this.total = total;
        }

    }

    private class FlipTask extends SwingWorker<Void, FlipPair> {
        @Override
        protected Void doInBackground() {
            long heads = 0;
            long total = 0;
            Random random = new Random();
            while (!isCancelled()) {
                total++;
                if (random.nextBoolean()) {
                    heads++;
                }
                publish(new FlipPair(heads, total));

            }
            return null;
        }

        @Override
        protected void process(List<FlipPair> pairs) {
            FlipPair pair = pairs.get(pairs.size() - 1);
            headsText.setText(String.format("%d", pair.heads));
            totalText.setText(String.format("%d", pair.total));
            devText.setText(String.format("%.10g", ((double) pair.heads) / ((double) pair.total) - 0.5));
            logger.info("是否EventDispatchThread-{}-{}", SwingUtilities.isEventDispatchThread(), Thread.currentThread().getId());
        }

    }

    public void actionPerformed(ActionEvent e) {
        if ("Start".equals(e.getActionCommand())) {
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
            (flipTask = new FlipTask()).execute();
        } else if ("Stop".equals(e.getActionCommand())) {
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
            flipTask.cancel(true);
            flipTask = null;
        }
        logger.info("actionPerformed-{}-{}", SwingUtilities.isEventDispatchThread(), Thread.currentThread().getId());

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Flipper::new);
    }
}