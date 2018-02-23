ruleset {

    // rulesets/basic.xml
    AssertWithinFinallyBlock
    AssignmentInConditional
    BigDecimalInstantiation
    BitwiseOperatorInConditional
    BooleanGetBoolean
    BrokenNullCheck
    BrokenOddnessCheck
    ClassForName
    ComparisonOfTwoConstants
    ComparisonWithSelf
    ConstantAssertExpression
    ConstantIfExpression
    ConstantTernaryExpression
    DeadCode
    DoubleNegative
    DuplicateCaseStatement
    DuplicateMapKey
    DuplicateSetValue
    EmptyCatchBlock
    'EmptyClass' doNotApplyToFilesMatching: '.*Spec.groovy'
    EmptyElseBlock
    EmptyFinallyBlock
    EmptyForStatement
    EmptyIfStatement
    EmptyInstanceInitializer
    EmptyMethod
    EmptyStaticInitializer
    EmptySwitchStatement
    EmptySynchronizedStatement
    EmptyTryBlock
    EmptyWhileStatement
    EqualsAndHashCode
    EqualsOverloaded
    ExplicitGarbageCollection
    ForLoopShouldBeWhileLoop
    HardCodedWindowsFileSeparator
    HardCodedWindowsRootDirectory
    IntegerGetInteger
    MultipleUnaryOperators
    RandomDoubleCoercedToZero
    RemoveAllOnSelf
    ReturnFromFinallyBlock
    ThrowExceptionFromFinallyBlock

    // rulesets/braces.xml
    ElseBlockBraces
    ForStatementBraces
    //IfStatementBraces
    WhileStatementBraces

    // rulesets/concurrency.xml
    BusyWait
    DoubleCheckedLocking
    InconsistentPropertyLocking
    InconsistentPropertySynchronization
    NestedSynchronization
    StaticCalendarField
    StaticConnection
    StaticDateFormatField
    StaticMatcherField
    StaticSimpleDateFormatField
    SynchronizedMethod
    SynchronizedOnBoxedPrimitive
    SynchronizedOnGetClass
    SynchronizedOnReentrantLock
    SynchronizedOnString
    SynchronizedOnThis
    SynchronizedReadObjectMethod
    SystemRunFinalizersOnExit
    ThisReferenceEscapesConstructor
    ThreadGroup
    ThreadLocalNotStaticFinal
    ThreadYield
    UseOfNotifyMethod
    VolatileArrayField
    VolatileLongOrDoubleField
    WaitOutsideOfWhileLoop

    // rulesets/convention.xml
    ConfusingTernary
    //CouldBeElvis
    HashtableIsObsolete
    IfStatementCouldBeTernary
    InvertedIfElse
    LongLiteralWithLowerCaseL
    //'NoDef' doNotApplyToFilesMatching: '.*Spec.groovy|.*Controller.groovy|.*GrailsPlugin.groovy'
    //ParameterReassignment
    TernaryCouldBeElvis
    VectorIsObsolete

    // rulesets/design.xml
    'AbstractClassWithPublicConstructor' enabled: false
    AbstractClassWithoutAbstractMethod
    BooleanMethodReturnsNull
    //BuilderMethodWithSideEffects
    CloneableWithoutClone
    CloseWithoutCloseable
    CompareToWithoutComparable
    ConstantsOnlyInterface
    EmptyMethodInAbstractClass
    FinalClassWithProtectedMember
    ImplementationAsType
    'Instanceof' enabled: false
    LocaleSetDefault
    //NestedForLoop
    'PrivateFieldCouldBeFinal' enabled: false
    PublicInstanceField
    ReturnsNullInsteadOfEmptyArray
    ReturnsNullInsteadOfEmptyCollection
    //SimpleDateFormatMissingLocale
    StatelessSingleton
    ToStringReturnsNull

    // rulesets/dry.xml
//    DuplicateListLiteral
//    DuplicateMapLiteral
//    DuplicateNumberLiteral
//    DuplicateStringLiteral

    // rulesets/enhanced.xml
    //CloneWithoutCloneable
    //JUnitAssertEqualsConstantActualValue
    //UnsafeImplementationAsMap

    // rulesets/exceptions.xml
    CatchArrayIndexOutOfBoundsException
    CatchError
    CatchException
    CatchIllegalMonitorStateException
    CatchIndexOutOfBoundsException
    CatchNullPointerException
    CatchRuntimeException
    CatchThrowable
    ConfusingClassNamedException
    ExceptionExtendsError
    ExceptionExtendsThrowable
    ExceptionNotThrown
    MissingNewInThrowStatement
    ReturnNullFromCatchBlock
    SwallowThreadDeath
    ThrowError
    ThrowException
    ThrowNullPointerException
    ThrowRuntimeException
    ThrowThrowable

    // rulesets/formatting.xml
    BlankLineBeforePackage
    BracesForClass
    BracesForForLoop
    BracesForIfElse
    BracesForMethod
    BracesForTryCatchFinally
    //ClassJavadoc
    ClosureStatementOnOpeningLineOfMultipleLineClosure
    ConsecutiveBlankLines
    //FileEndsWithoutNewline
    //'LineLength' doNotApplyToFilesMatching: '*Spec.groovy'
    MissingBlankLineAfterImports
    MissingBlankLineAfterPackage
    //SpaceAfterCatch
    //SpaceAfterFor
    //SpaceAfterIf
    //SpaceAfterSwitch
    //SpaceAfterWhile
    //SpaceAroundClosureArrow
    //SpaceAroundMapEntryColon
    //SpaceAroundOperator
    //SpaceAfterClosingBrace
    SpaceAfterComma
    //SpaceAfterOpeningBrace
    SpaceAfterSemicolon
    //SpaceBeforeClosingBrace
    //SpaceBeforeOpeningBrace
    //TrailingWhitespace

    // rulesets/generic.xml
    IllegalClassMember
    IllegalClassReference
    IllegalPackageReference
    IllegalRegex
    IllegalString
    IllegalSubclass
    RequiredRegex
    RequiredString
    StatelessClass

    // rulesets/groovyism.xml
    AssignCollectionSort
    AssignCollectionUnique
    ClosureAsLastMethodParameter
    CollectAllIsDeprecated
    ConfusingMultipleReturns
    ExplicitArrayListInstantiation
    ExplicitCallToAndMethod
    ExplicitCallToCompareToMethod
    ExplicitCallToDivMethod
    ExplicitCallToEqualsMethod
    ExplicitCallToGetAtMethod
    ExplicitCallToLeftShiftMethod
    ExplicitCallToMinusMethod
    ExplicitCallToModMethod
    ExplicitCallToMultiplyMethod
    ExplicitCallToOrMethod
    ExplicitCallToPlusMethod
    ExplicitCallToPowerMethod
    ExplicitCallToRightShiftMethod
    ExplicitCallToXorMethod
    ExplicitHashMapInstantiation
    ExplicitHashSetInstantiation
    ExplicitLinkedHashMapInstantiation
    ExplicitLinkedListInstantiation
    ExplicitStackInstantiation
    ExplicitTreeSetInstantiation
    GStringAsMapKey
    GStringExpressionWithinString
    //GetterMethodCouldBeProperty
    GroovyLangImmutable
    UseCollectMany
    UseCollectNested

    // rulesets/imports.xml
    DuplicateImport
    ImportFromSamePackage
    ImportFromSunPackages
    //MisorderedStaticImports
    //'NoWildcardImports' doNotApplyToFilesMatching: '.*Spec.groovy'
    UnnecessaryGroovyImport
    UnusedImport

    // rulesets/jdbc.xml
    DirectConnectionManagement
    JdbcConnectionReference
    JdbcResultSetReference
    JdbcStatementReference

    // rulesets/junit.xml
    ChainedTest
    CoupledTestCase
    JUnitAssertAlwaysFails
    JUnitAssertAlwaysSucceeds
    JUnitFailWithoutMessage
    JUnitLostTest
    JUnitPublicField
    JUnitPublicNonTestMethod
    //JUnitPublicProperty
    JUnitSetUpCallsSuper
    JUnitStyleAssertions
    JUnitTearDownCallsSuper
    JUnitTestMethodWithoutAssert
    JUnitUnnecessarySetUp
    JUnitUnnecessaryTearDown
    JUnitUnnecessaryThrowsException
    SpockIgnoreRestUsed
    UnnecessaryFail
    UseAssertEqualsInsteadOfAssertTrue
    UseAssertFalseInsteadOfNegation
    UseAssertNullInsteadOfAssertEquals
    UseAssertSameInsteadOfAssertTrue
    UseAssertTrueInsteadOfAssertEquals
    UseAssertTrueInsteadOfNegation

    // rulesets/logging.xml
    LoggerForDifferentClass
    LoggerWithWrongModifiers
    LoggingSwallowsStacktrace
    MultipleLoggers
    PrintStackTrace
    //Println
    SystemErrPrint
    SystemOutPrint

    // rulesets/naming.xml
    AbstractClassName
    ClassName
    ClassNameSameAsFilename
    //ConfusingMethodName
    //'FactoryMethodName' doNotApplyToFilesMatching: '.*Spec.groovy'
    FieldName
    InterfaceName
    'MethodName' doNotApplyToFilesMatching: '.*Spec.groovy'
    ObjectOverrideMisspelledMethodName
    PackageName
    PackageNameMatchesFilePath
    ParameterName
    PropertyName
    VariableName

    // rulesets/security.xml
    FileCreateTempFile
    InsecureRandom
    'JavaIoPackageAccess' enabled: false
    NonFinalPublicField
    NonFinalSubclassOfSensitiveInterface
    ObjectFinalize
    PublicFinalizeMethod
    SystemExit
    UnsafeArrayDeclaration

    // rulesets/serialization.xml
    EnumCustomSerializationIgnored
    SerialPersistentFields
    SerialVersionUID
    'SerializableClassMustDefineSerialVersionUID' enabled: false

    // rulesets/size.xml
    AbcComplexity   // DEPRECATED: Use the AbcMetric rule instead. Requires the GMetrics jar
    'AbcMetric' doNotApplyToFilesMatching: '.*Spec.groovy'   // Requires the GMetrics jar
    ClassSize
    //CrapMetric   // Requires the GMetrics jar and a Cobertura coverage file
    //CyclomaticComplexity
    MethodCount
    'MethodSize' doNotApplyToFilesMatching: '.*Spec.groovy'
    NestedBlockDepth
    //'ParameterCount' maxParameters: 6

    // rulesets/unnecessary.xml
    AddEmptyString
    ConsecutiveLiteralAppends
    //ConsecutiveStringConcatenation
    UnnecessaryBigDecimalInstantiation
    UnnecessaryBigIntegerInstantiation
    'UnnecessaryBooleanExpression' doNotApplyToFilesMatching: '.*Spec.groovy'
    UnnecessaryBooleanInstantiation
    UnnecessaryCallForLastElement
    UnnecessaryCallToSubstring
    UnnecessaryCast
    UnnecessaryCatchBlock
    UnnecessaryCollectCall
    UnnecessaryCollectionCall
    UnnecessaryConstructor
    UnnecessaryDefInFieldDeclaration
    //UnnecessaryDefInMethodDeclaration
    UnnecessaryDefInVariableDeclaration
    //UnnecessaryDotClass
    UnnecessaryDoubleInstantiation
    UnnecessaryElseStatement
    UnnecessaryFinalOnPrivateMethod
    UnnecessaryFloatInstantiation
    //UnnecessaryGString
    //UnnecessaryGetter
    UnnecessaryIfStatement
    UnnecessaryInstanceOfCheck
    UnnecessaryInstantiationToGetClass
    UnnecessaryIntegerInstantiation
    UnnecessaryLongInstantiation
    UnnecessaryModOne
    UnnecessaryNullCheck
    UnnecessaryNullCheckBeforeInstanceOf
    'UnnecessaryObjectReferences' doNotApplyToFilesMatching: '.*Spec.groovy'
    UnnecessaryOverridingMethod
    UnnecessaryPackageReference
    UnnecessaryParenthesesForMethodCallWithClosure
    //UnnecessaryPublicModifier
    //UnnecessaryReturnKeyword
    UnnecessarySafeNavigationOperator
    UnnecessarySelfAssignment
    UnnecessarySemicolon
    UnnecessaryStringInstantiation
    //UnnecessarySubstring
    UnnecessaryTernaryExpression
    //UnnecessaryToString
    UnnecessaryTransientModifier

    // rulesets/unused.xml
    UnusedArray
    'UnusedMethodParameter' enabled: false
    UnusedObject
    UnusedPrivateField
    UnusedPrivateMethod
    UnusedPrivateMethodParameter
    //UnusedVariable

    // Formatting
    NoTabCharacter

    ruleset('rulesets/codenarc-extra.xml') {
        CompileStatic  {
            //doNotApplyToClassNames = '*Spec,*Test,.*GrailsPlugin,Application'
            doNotApplyToFilesMatching = ".*/src/test/.*|.*GrailsPlugin.groovy|.*Application.groovy|.*BootStrap.groovy"
        }
    }
}
