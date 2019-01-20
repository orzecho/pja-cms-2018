/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { ExamResultDeleteDialogComponent } from 'app/entities/exam-result/exam-result-delete-dialog.component';
import { ExamResultService } from 'app/entities/exam-result/exam-result.service';

describe('Component Tests', () => {
    describe('ExamResult Management Delete Component', () => {
        let comp: ExamResultDeleteDialogComponent;
        let fixture: ComponentFixture<ExamResultDeleteDialogComponent>;
        let service: ExamResultService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [ExamResultDeleteDialogComponent]
            })
                .overrideTemplate(ExamResultDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ExamResultDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ExamResultService);
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
