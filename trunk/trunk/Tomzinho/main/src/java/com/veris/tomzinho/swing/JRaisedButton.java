/*
 *  COPYRIGHT 2005 Sanmina-SCI Corporation
 *  ALL RIGHTS RESERVED - TODOS OS DIREITOS RESERVADOS
 *  CONFIDENTIAL, UNPUBLISHED PROPERTY OF Sanmina-SCI Corporation.
 *  PROPRIEDADE CONFIDENCIAL NAO PUBLICADA DA Sanmina-SCI Corporation. 
 * 
 *  created in Mar 21, 2005
 * 
 *  @author Mariana Baptistella
 */
package com.veris.tomzinho.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 *  ==> COMMENT THIS CLASS
 *  
 *  
 * @author <a href="mailto:Mariana.Baptistella@sanmina-sci.com">Mariana Baptistella</a>
 * @version $Revision: 714 $
 */
public class JRaisedButton extends JButton implements MouseListener {

    /**
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 4120853256936306228L;
	private boolean createBorder;

    /**
     * @see JButton#JButton()
     */
    public JRaisedButton() {
        super();
        this.setCustomProperties();
    }

    /**
     * @param icon
     * @see JButton#JButton(javax.swing.Icon)
     */
    public JRaisedButton(Icon icon) {
        super(icon);
        this.setCustomProperties();
    }

    /**
     * @param text
     * @see JButton#JButton(java.lang.String)
     */
    public JRaisedButton(String text) {
        super(text);
        this.setCustomProperties();
    }

    /**
     * @param a
     * @see JButton#JButton(javax.swing.Action)
     */
    public JRaisedButton(Action a) {
        super(a);
        this.setCustomProperties();
    }

    /**
     * @param a
     * @see JButton#JButton(javax.swing.Action)
     */
    public JRaisedButton(Action a, boolean createBorder) {
        super(a);
        this.createBorder = createBorder;
        this.setCustomProperties();
    }

    
    /**
     * @param text
     * @param icon
     * @see JButton#JButton(java.lang.String, javax.swing.Icon)
     */
    public JRaisedButton(String text, Icon icon) {
        super(text, icon);
        this.setCustomProperties();
    }

    
    /**
     * Set the custom properties to this button.
     * 
     */
    private void setCustomProperties() {
    	Border border;
    	
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        if (this.createBorder) {
        	Border outsideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        	Border insideBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        	border = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        } else {
        	border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        }
        
        this.setBorder(border);
        this.setBorderPainted(false);
        this.setHorizontalAlignment(SwingConstants.LEFT);
        this.addMouseListener(this);
        this.addActionListener(new ActionListener() {
        
            public void actionPerformed(ActionEvent e) {
                ((JButton)e.getSource()).setBorderPainted(false);   
        
            }
        
        });
        this.addFocusListener(new FocusListener() {
		
			public void focusLost(FocusEvent e) {
				setBorderPainted(false);
		
			}
		
			public void focusGained(FocusEvent e) {
				setBorderPainted(true);
		
			}
		
		});
    }

    /**
     * @see MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        //on mouse over shows the border raised
        ((JButton)e.getSource()).setBorderPainted(true);        
    }

    /**
     * @see MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        ((JButton)e.getSource()).setBorderPainted(false);        
    }

    /**
     * @see MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * @see MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        Border border;
    	if (this.createBorder) {
        	Border outsideBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        	Border insideBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        	border = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        } else {
        	border = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        }
    	
        this.setBorder(border);
        ((JButton)e.getSource()).setBorderPainted(true);        
    }

    /**
     * @see MouseListener#mouseReleased(java.awt.event.MouseEvent)     */
    public void mouseReleased(MouseEvent e) {
    	Border border;
    	if (this.createBorder) {
        	Border outsideBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        	Border insideBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        	border = BorderFactory.createCompoundBorder(outsideBorder, insideBorder);
        } else {
        	border = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        }
    	this.setBorder(border);

    }    

}
