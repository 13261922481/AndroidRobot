package com.android.robot;

import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.android.util.PropertiesUtil;

public class SetApkWindow extends Dialog {

    protected String  result;
    protected Shell   shell;
    protected Display display;

    private Text      textDir;
    @SuppressWarnings("unused")
    private Text      textScript;
    @SuppressWarnings("unused")
    private Label     lblError;
    private Button    btnForceInstall;
    private Button    btnIsSelendroid;
    private Button    btnIsChromedriver;

    private String    aut            = "";
    private boolean   isSelendroid   = false;
    private boolean   isChromedriver = false;
    private boolean   isForceInstall = false;

    private String    path;

    /**
     * Create the dialog.
     * @param parent
     * @param style
     */
    public SetApkWindow(Shell parent, int style, String path) {
        super(parent, style);
        setText("设置被测应用");
        this.path = path;
        loadParams();
    }

    private void loadParams() {
        aut = PropertiesUtil.getValue(path, "aut");
        try {
            isSelendroid = Boolean.parseBoolean(PropertiesUtil.getValue(path, "isSelendroid"));
        } catch (Exception ex) {
            isSelendroid = false;
        }

        try {
            isChromedriver = Boolean.parseBoolean(PropertiesUtil.getValue(path, "isChromedriver"));
        } catch (Exception ex) {
            isChromedriver = false;
        }

        try {
            isForceInstall = Boolean.parseBoolean(PropertiesUtil.getValue(path, "isForceInstall"));
        } catch (Exception ex) {
            isForceInstall = false;
        }
    }

    /**
     * Open the dialog.
     * @return the result
     */
    public String open() {
        createContents();

        GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_label.widthHint = 434;

        Composite composite2 = new Composite(shell, SWT.NONE);
        composite2.setLayoutData(new GridData(435, 50));
        composite2.setLayout(new GridLayout(3, false));

        Label lb = new Label(composite2, SWT.CENTER);
        lb.setText("选择应用:");

        textDir = new Text(composite2, SWT.BORDER);
        GridData gd_textDir = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
        gd_textDir.widthHint = 270;
        textDir.setLayoutData(gd_textDir);
        textDir.setText(aut);

        Composite composite5 = new Composite(shell, SWT.NONE);
        composite5.setLayoutData(new GridData(435, 100));
        composite5.setLayout(new GridLayout(1, false));
        btnForceInstall = new Button(composite5, SWT.CHECK);
        btnForceInstall.setText("强制安装");
        btnForceInstall.setSelection(isForceInstall);

        btnIsSelendroid = new Button(composite5, SWT.CHECK);
        btnIsSelendroid.setText("使用Selendroid");
        btnIsSelendroid.setSelection(isSelendroid);

        btnIsChromedriver = new Button(composite5, SWT.CHECK);
        btnIsChromedriver.setText("使用Chromedriver");
        btnIsChromedriver.setSelection(isChromedriver);

        Button selectApp = new Button(composite2, SWT.CENTER);
        selectApp.setText("选择");
        selectApp.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                FileDialog dialog = new FileDialog(shell, SWT.MULTI | SWT.OPEN);
                dialog.setFilterNames(new String[] { "apk Files (*.apk)" });
                dialog.setFilterExtensions(new String[] { "*.apk*" });
                dialog.setFilterPath(".\\workspace");
                String choice = dialog.open();
                if (choice != null) {
                    String filePath = dialog.getFilterPath();
                    String[] selectedFiles = dialog.getFileNames();
                    File apkFile = new File(filePath + "/" + selectedFiles[0]);
                    textDir.setText(apkFile.getAbsolutePath());
                }
            }
        });

        Composite composite3 = new Composite(shell, SWT.NONE);
        GridData gd_composite3 = new GridData(435, 50);
        gd_composite3.verticalAlignment = SWT.FILL;
        composite3.setLayoutData(gd_composite3);
        composite3.setLayout(new GridLayout(2, false));

        Button previous = new Button(composite3, SWT.NONE);
        GridData gd_previous = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_previous.heightHint = 25;
        gd_previous.widthHint = 77;
        previous.setLayoutData(gd_previous);
        previous.setText("确定");
        previous.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                PropertiesUtil.append(path, "aut", textDir.getText().trim(), "Aut");
                PropertiesUtil.append(path, "isForceInstall", btnForceInstall.getSelection(), "");
                PropertiesUtil.append(path, "isSelendroid", btnIsSelendroid.getSelection(), "");
                PropertiesUtil.append(path, "isChromedriver", btnIsChromedriver.getSelection(), "");
                result = "";
                shell.close();
                shell.dispose();
            }
        });

        Button cancel = new Button(composite3, SWT.NONE);
        GridData gd_cancel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
        gd_cancel.heightHint = 25;
        gd_cancel.widthHint = 77;
        cancel.setLayoutData(gd_cancel);
        cancel.setText("取消");

        cancel.addSelectionListener(new SelectionAdapter() {
            public void widgetSelected(SelectionEvent e) {
                result = "";
                shell.close();
                shell.dispose();
            }
        });

        shell.open();
        shell.layout();

        while (shell != null && !shell.isDisposed()) {
            if (display != null && !display.readAndDispatch()) {
                display.sleep();
            }
        }
        return result;
    }

    /**
     * Create contents of the dialog.
     */
    private void createContents() {
        display = getParent().getDisplay();
        shell = new Shell(getParent(), getStyle());
        shell.setSize(450, 300);
        shell.setText(getText());
        shell
            .setImage(new Image(display, ClassLoader.getSystemResourceAsStream("icons/title.png")));
        shell.setLayout(new GridLayout(1, false));

    }

}
