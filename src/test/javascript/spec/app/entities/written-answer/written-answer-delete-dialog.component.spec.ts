/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { NyanTestModule } from '../../../test.module';
import { WrittenAnswerDeleteDialogComponent } from 'app/entities/written-answer/written-answer-delete-dialog.component';
import { WrittenAnswerService } from 'app/entities/written-answer/written-answer.service';

describe('Component Tests', () => {
    describe('WrittenAnswer Management Delete Component', () => {
        let comp: WrittenAnswerDeleteDialogComponent;
        let fixture: ComponentFixture<WrittenAnswerDeleteDialogComponent>;
        let service: WrittenAnswerService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NyanTestModule],
                declarations: [WrittenAnswerDeleteDialogComponent]
            })
                .overrideTemplate(WrittenAnswerDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(WrittenAnswerDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WrittenAnswerService);
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
