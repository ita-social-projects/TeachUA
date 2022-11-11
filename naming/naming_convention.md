### Actions:
`click<ElementName>` - click on particular element  
`clear<ElementName>` - clear text field  
`send<ElementName>Text` - send text to a specific field  
`create<ComponentName>` - create component  
`close<ComponentName>` - close component  
`get<ElementName>` - get element  
`get<ElementName>Text` - get element text  
`get<ElementName>AttributeValue` - get element attribute value
`get<ElementName>Value` - get element numeric value  
`get<ElementGroupName>Count` - get element group size  
`print<ElementGroupName>` - print element group  
`goto<PageName>Page` - go to a certain page  
`scrollto<ElementName>` - scroll to element
`is<ElementName>Enabled` - check if element is enabled  
`is<ElementName>Exist` - check if element exists  

### File names:
`<PageName>Page` - functionality of the whole page  
`<WindowName>Window` - separate window which does not extend any class  
`<PartName>Part` - only specific functionality on the page  
`<ComponentName>Component` - entity on the page that present multiple times on the page but with different information inside  
`<GroupName>Container` - a group of items that have something common  
`<ComponentName>Repository` - contains predefined values that can be used somewhere else  

### Tests:
`<TestName>Test` - perform some actions to check if certain functionality works correctly  