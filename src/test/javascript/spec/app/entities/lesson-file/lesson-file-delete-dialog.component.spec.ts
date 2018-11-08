/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { LessonFileDeleteDialogComponent } from 'app/entities/lesson-file/lesson-file-delete-dialog.component';
import { LessonFileService } from 'app/entities/lesson-file/lesson-file.service';

describe('Component Tests', () => {
    describe('LessonFile Management Delete Component', () => {
        let comp: LessonFileDeleteDialogComponent;
        let fixture: ComponentFixture<LessonFileDeleteDialogComponent>;
        let service: LessonFileService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [LessonFileDeleteDialogComponent]
            })
                .overrideTemplate(LessonFileDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(LessonFileDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LessonFileService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it(
                'Should call delete service on confirmDelete',
                inject(
                    [],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });
});
