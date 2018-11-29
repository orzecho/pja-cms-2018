import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { NyanSharedModule } from 'app/shared';
import { testRoute } from 'app/entities/test/test.route';
import { TestComponent } from 'app/entities/test/test.component';
import { VocabularyTestComponent } from 'app/entities/test/vocabulary-test.component';
import { AutoCompleteModule, InputTextModule, RadioButtonModule } from 'primeng/primeng';

const ENTITY_STATES = [...testRoute];

@NgModule({
    imports: [NyanSharedModule, RouterModule.forChild(ENTITY_STATES), RadioButtonModule, InputTextModule, AutoCompleteModule],
    declarations: [TestComponent, VocabularyTestComponent],
    entryComponents: [TestComponent, VocabularyTestComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NyanTestModule {}
