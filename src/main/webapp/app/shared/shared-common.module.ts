import { NgModule } from '@angular/core';

import { NyanSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
    imports: [NyanSharedLibsModule],
    declarations: [JhiAlertComponent, JhiAlertErrorComponent],
    exports: [NyanSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class NyanSharedCommonModule {}
