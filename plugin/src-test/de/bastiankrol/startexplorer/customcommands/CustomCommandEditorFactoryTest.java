package de.bastiankrol.startexplorer.customcommands;

import static de.bastiankrol.startexplorer.customcommands.CommandConfigObjectMother.oneForBoth;
import static de.bastiankrol.startexplorer.customcommands.CommandConfigObjectMother.oneForBothOneForResourceOneForEditor;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.eclipse.core.commands.Command;
import org.eclipse.jface.action.IContributionItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import de.bastiankrol.startexplorer.preferences.PreferenceModel;

@RunWith(PowerMockRunner.class)
@PrepareForTest(Command.class)
public class CustomCommandEditorFactoryTest extends
    AbstractCustomCommandFactoryTest
{

  @Override
  AbstractCustomCommandFactory createFactory()
  {
    return new CustomCommandEditorFactory()
    {
      @Override
      PreferenceModel getPreferenceModel()
      {
        return preferenceModelMock;
      }
    };
  }

  @Test
  public void oneCommandForEditor() throws Exception
  {
    // given a configuration with only one command config for editor
    when(this.preferenceModelMock.getCommandConfigList()).thenReturn(
        oneForBoth());
    // when getContributionItems is called by the Eclipse platform
    IContributionItem[] contributionItems = this.customCommandFactory
        .getContributionItems();
    // it should return one contribution item
    assertEquals(1, contributionItems.length);
    verify(this.customCommandFactory).createContributionItem(
        this.parameterCaptor.capture());
    assertEquals("command/both/editor", this.parameterCaptor.getAllValues()
        .get(0).label);
  }

  @Test
  public void twoCommandsForEditorOneResourceViewCommandToBeIgnored()
      throws Exception
  {
    // given a configuration with two command configs for editor and one only
    // for resource view
    when(this.preferenceModelMock.getCommandConfigList()).thenReturn(
        oneForBothOneForResourceOneForEditor());
    // when getContributionItems is called by the Eclipse platform
    IContributionItem[] contributionItems = this.customCommandFactory
        .getContributionItems();
    // it should return two contribution items
    assertEquals(2, contributionItems.length);
    verify(this.customCommandFactory, times(2)).createContributionItem(
        this.parameterCaptor.capture());
    assertEquals("command/both/editor", this.parameterCaptor.getAllValues()
        .get(0).label);
    assertEquals("command/editor",
        this.parameterCaptor.getAllValues().get(1).label);
  }

  @Test
  public void testCleanUp() throws Exception
  {
    commonTestCleanUp();
  }
}
