/*
 * Copyright 2013, 2014 Mauricio Soares da Silva.
 *
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Tradução não-oficial:
 *
 * Este programa é um software livre; você pode redistribuí-lo e/ou
 *   modificá-lo dentro dos termos da Licença Pública Geral GNU como
 *   publicada pela Fundação do Software Livre (FSF); na versão 3 da
 *   Licença, ou (na sua opinião) qualquer versão.
 *
 *   Este programa é distribuído na esperança de que possa ser útil,
 *   mas SEM NENHUMA GARANTIA; sem uma garantia implícita de ADEQUAÇÃO
 *   a qualquer MERCADO ou APLICAÇÃO EM PARTICULAR. Veja a
 *   Licença Pública Geral GNU para maiores detalhes.
 *
 *   Você deve ter recebido uma cópia da Licença Pública Geral GNU junto
 *   com este programa. Se não, veja <http://www.gnu.org/licenses/>.
 */
package br.com.yahoo.mau_mss.browsefolder;

import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Title: BrowseFolder
 * Description: Plugin para abrir o caminho de um arquivo no explorer
 * Date: March 29, 2013, 11:08:00 AM
 *
 * @author Mauricio Soares da Silva (mauricio.soares)
 */
@ActionID(category = "File",
        id = "br.com.yahoo.mau_mss.browsefolder.BrowseFolder")
@ActionRegistration(iconBase = "resources/open.png",
        displayName = "#CTL_BrowseFolder")
@ActionReferences({
  @ActionReference(path = "Menu/Tools", position = 190),
  @ActionReference(path = "Editors/text/x-java/Popup", position = 3030)
})
@Messages("CTL_BrowseFolder=Browse Folder")
public class BrowseFolder implements ActionListener {
  private final DataObject dataObject;

  public BrowseFolder(DataObject context) {
    this.dataObject = context;
  }

  @Override
  public void actionPerformed(ActionEvent ev) {
    FileObject fileObject = this.dataObject.getPrimaryFile();
    if (fileObject == null) {
      return;
    }
    StatusDisplayer.getDefault().setStatusText("Path: " + fileObject.getPath());
    try {
      if (BrowseFolder.isWindows()) {
        new ProcessBuilder("explorer.exe", "/select," + fileObject.getPath().replace("/", "\\")).start();
      } else {
        Desktop.getDesktop().open(new File(fileObject.getPath()));
      }
      // para Gnome poderia ser utilizado gnome-open
    } catch (IOException ex) {
      Exceptions.printStackTrace(ex);
    }
  }

  public static boolean isWindows() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("win"));
  }

  public static boolean isMac() {
    String os = System.getProperty("os.name").toLowerCase();
    return (os.contains("mac"));
  }

  public static boolean isUnix() {
    String os = System.getProperty("os.name").toLowerCase();
    // linux ou unix
    return (os.contains("nix") || os.contains("nux"));
  }

}
